package ee.sk.hwcrypto.demo.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ee.sk.hwcrypto.demo.model.Isik;

//test.html kasutab

@RestController
public class JqueryTestController {
	
	@RequestMapping(value="/demo", method = RequestMethod.GET)
    public String demoget(@RequestParam(value="name", required = false, defaultValue="World") String name) {
//		try {
//			TimeUnit.SECONDS.sleep(10);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return name;	
	}

	@RequestMapping(value="/memo", method = RequestMethod.GET)
    public String memoget(@RequestParam(value="name", required = false, defaultValue="Maailm") String name) {
		return name;	
	}
	
	
	@RequestMapping(value="/then", method = RequestMethod.POST) //,consumes = "text/plain"
	@ResponseBody //Annotation that indicates a method return value should be bound to the web response body. 
	//Supported for annotated handler methods.
    public String thenget(@RequestBody String tekst) {
		System.out.println("POST: "+tekst);
		Isik vast = new Isik("Then","");
		vast.setCity(tekst);
		return vast.getCity();	
	}
	

	@RequestMapping(value="/demo", method = RequestMethod.POST) //,consumes = "text/plain"
	@ResponseBody //Annotation that indicates a method return value should be bound to the web response body. 
	//Supported for annotated handler methods.
    public Isik testpost(@RequestBody Isik inimene) {
		System.out.println("Post: "+inimene.getCity());
		System.out.println("Post: "+inimene.getName());
		Isik vast = new Isik();
		vast.setCity(inimene.getCity());
		vast.setName(inimene.getName());
		return vast;	
	}	
}
