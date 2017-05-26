package bootsample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bootsample.dao.PostRepository;
import bootsample.model.Post;
import bootsample.service.S3Service;
import bootsample.service.StorageFileNotFoundException;
import bootsample.service.StorageService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Controller
public class FileUploadController {

	@Autowired
	private PostRepository postRepository;

	private final StorageService storageService;

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {
		Post post = new Post();
		model.addAttribute("post", post);
		return "index";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}
	@GetMapping("/download")
	public String download(@RequestParam("url") String url,Model model,HttpServletResponse response,HttpServletRequest request) throws IOException {
		String[] parts = url.split("/");
		String bucketName =parts[3];
		String key = parts[4];
		S3Service s3 = new S3Service();
		InputStream input = s3.getFile(bucketName, key);
		//String filePath = System.getProperty("user.dir")+"/upload-dir/" + key;
		String path =  request.getRealPath("/")+"upload-dir"+ File.separator+ key;
		System.out.println("/download: "+path);
		String filePath = storageService.store(path,input,key);
	    System.out.println(filePath);
	    
	    File file = new File(filePath);
	    String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        if(mimeType==null){
            System.out.println("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }
        InputStream is = new FileInputStream(file);
        // MIME type of the file
        response.setContentType("application/octet-stream");
        // Response header
        response.setHeader("Content-Disposition", "attachment; filename=\""
                + file.getName() + "\"");
        // Read from the file and write into the response
        OutputStream os = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        os.flush();
        os.close();
        is.close();
	    Post post = new Post();
		model.addAttribute("post", post);
		return "redirect:/";
	}
	@GetMapping("/{id}")
	public String findById(@PathVariable("id")Integer id, Model model) {
		Post post = postRepository.findOne(id);
		model.addAttribute("post", post);
		return "index";
	}
	
	@RequestMapping(method = RequestMethod.POST, value={"/"})
	public String insert(ModelMap model,HttpServletRequest request,
			@ModelAttribute("post")Post post,@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
					throws Exception {
		Path path = Paths.get(request.getRealPath("/")+"upload-dir"+File.separator+file.getOriginalFilename());
		String fileName = storageService.store(path,file);
		S3Service s3 = new S3Service();
		//String path = System.getProperty("user.dir")+"/upload-dir/" + fileName;
		String pathFile = request.getRealPath("/")+"upload-dir"+ File.separator+fileName;
		System.out.println(pathFile);
		/*File f = new File(path);*/
		//String url = upload.excuteUploadFile(path);
		String url = s3.uploadS3(pathFile);
		post.setUrl(url);
		postRepository.save(post);
		return "redirect:/";
	}
	
	@ModelAttribute("posts")
	public List<Post> getDocuments(){
		return (List<Post>) postRepository.findAll();
	}
}
