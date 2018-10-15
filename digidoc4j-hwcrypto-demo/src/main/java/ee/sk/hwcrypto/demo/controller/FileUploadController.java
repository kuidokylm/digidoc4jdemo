package ee.sk.hwcrypto.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.digidoc4j.Configuration;
import org.digidoc4j.Container;
import org.digidoc4j.ContainerBuilder;
import org.digidoc4j.Signature;
import org.digidoc4j.SignatureValidationResult;
import org.digidoc4j.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ee.sk.hwcrypto.demo.model.BdocCheck;
import ee.sk.hwcrypto.demo.model.BdocFailid;
import ee.sk.hwcrypto.demo.storage.StorageFileNotFoundException;
import ee.sk.hwcrypto.demo.storage.StorageService;

@Controller
public class FileUploadController {

	//https://springframework.guru/using-logback-spring-boot/
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/failid")
    public String listUploadedFiles(Model model) throws IOException {
    	ArrayList<BdocFailid> bdf = storageService.getFailid();
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));
        
        model.addAttribute("bfailid",bdf);
        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    //@PostMapping(value= {"/files"}) //spring.servlet.multipart.enabled=true
    //HttpServletRequest req,
    //@RequestMapping(value= {"/files"} , method=RequestMethod.POST,consumes="multipart/form-data")
    @PostMapping(value= {"/files"},consumes="multipart/form-data")
    public String handleFileUpload(@RequestParam(value="fail", required = true) MultipartFile file,RedirectAttributes redirectAttributes,Model model)
    {    	
        storageService.store(file);
        if (file == null)
        {
            redirectAttributes.addFlashAttribute("message","Faili ei laetud ülesse");        	
        }
        else
        {
        	BdocCheck bcheck = new BdocCheck(false); 
        	byte[] byteArr;
			try {
				byteArr = file.getBytes();
				InputStream inputStream = new ByteArrayInputStream(byteArr);
				redirectAttributes.addFlashAttribute("message","You successfully uploaded " + file.getOriginalFilename());
				
				//Configuration configuration = new Configuration(Configuration.Mode.PROD);
				
				
				Container container = ContainerBuilder.
					    aContainer("BDOC").  // Container type is BDoc
					    //withConfiguration(this.configuration).  // Using our configuration
					    fromStream(inputStream).build();
				
				//valideerime konteinerit
				String result="";
				ValidationResult vr = container.validate();
				bcheck.setCheck(vr.isValid());
				if (!vr.isValid())
				{					
					for (int j = 0; j < container.getSignatures().size(); j++) {
						
						Signature sig = container.getSignatures().get(j);
						ValidationResult sres = sig.validateSignature();
						if (!sres.isValid())
						{							
							if (sres.getErrors().size() > 0)
							{
								result=" SignatureErrors: ";
				 			    result+= sres.getErrors().stream()
									      .map(n -> n.getMessage())
									      .collect(Collectors.joining(",", "{ ", " }"));
				 			   logger.error(result);
				 			   redirectAttributes.addFlashAttribute("sigerrors",result);
							}
							if (sres.getWarnings().size() > 0)
							{
								result=" SignatureWarnings: ";
								result+= sres.getWarnings().stream()
									      .map(n -> n.getMessage())
									      .collect(Collectors.joining(",", "{ ", " }"));
								logger.error(result);
								redirectAttributes.addFlashAttribute("sigwarnings",result);
							}
						}
					}
					//.stream().map(s -> s.validateSignature())
					
					if (vr.getErrors().size() > 0)
					{
		 			    result = vr.getErrors().stream()
							      .map(n -> n.getMessage())
							      .collect(Collectors.joining(",", "{ ", " }"));
		 			    logger.error(result);
						redirectAttributes.addFlashAttribute("verrors",result);
					}
					
					if (vr.getWarnings().size() > 0)
					{
						result = vr.getWarnings().stream()
							      .map(n -> n.getMessage())
							      .collect(Collectors.joining(",", "{ ", " }"));
						logger.error(result);
						redirectAttributes.addFlashAttribute("vwarnings",result);
					}

				}
				int kokku=container.getDataFiles().size();
				kokku= container.getSignatures().size();
				ArrayList<BdocFailid> bdf = new ArrayList<BdocFailid>(); 
				if (kokku > 0 )
				{
					for (int i = 0; i < container.getDataFiles().size(); i++) {
						BdocFailid bd = new BdocFailid();
						bd.setFilename(container.getDataFiles().get(i).getName());
						//bd.setFilebytes(container.getDataFiles().get(i).getBytes());
						bdf.add(bd);
					}
					bcheck.setFailid(bdf);
					
					//model.addAttribute("bfailid",bdf);
					//redirectAttributes.addAttribute("bfailid",bdf); //siin viga
					Signature si=container.getSignatures().get(0);
					
					bcheck.setCommonname(si.getSigningCertificate().getX509Certificate().getSubjectDN().getName());
					redirectAttributes.addFlashAttribute("signature","DNname: "+bcheck.getCommonname());
					redirectAttributes.addFlashAttribute("bmess","OCSP: "+si.getOCSPCertificate().getX509Certificate().getSubjectDN().getName());
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("IOException "+e.getMessage());
				redirectAttributes.addFlashAttribute("message","FileUploadController.handleFileUpload IOException "+e.getMessage());
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				redirectAttributes.addFlashAttribute("message","FileUploadController.handleFileUpload Exception "+e.getMessage());
			}
        }
        //return "uploadForm";
        return "redirect:/failid";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}