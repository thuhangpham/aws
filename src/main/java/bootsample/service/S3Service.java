package bootsample.service;

import org.omg.CORBA.portable.InputStream;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class S3Service {
	private AmazonS3 s3=null;
	//private AWSCredentials credentials = null;
	private BasicAWSCredentials credentials =null;
	public S3Service(){
	        try {
	        	credentials = new BasicAWSCredentials("AKIAJXFG5XDBTYYKCXTQ", "7ZD7KuITWmIJlvtYTzf1hHllqfdrqhubRNFMjZSu");
	        	/*AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	        	                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
	        	                        .build();*/
	            //credentials = new ProfileCredentialsProvider().getCredentials(); // cua nguoi ta
	        } catch (Exception e) {
	            throw new AmazonClientException(
	                    "Cannot load the credentials from the credential profiles file. " +
	                    "Please make sure that your credentials file is at the correct " +
	                    "location (~/.aws/credentials), and is in valid format.",
	                    e);
	        }

	        s3 = new AmazonS3Client(credentials);
//	        s3 =  AmazonS3ClientBuilder.standard()
//                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                    .build();
	        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
	        s3.setRegion(usWest2);
	}
	public java.io.InputStream getFile(String bucketName, String key) {
		S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
		  return object.getObjectContent();
	}
	public String uploadS3(String path) {
	        String bucketName = "g17";
	    	java.io.File f = new java.io.File(path);

	        String key = f.getName();
	        // upload private
	       // s3.putObject(new PutObjectRequest(bucketName, key,f));
	        // upload public
	        s3.putObject(
					new PutObjectRequest(bucketName, f.getName(), f).withCannedAcl(CannedAccessControlList.PublicRead));
	        String URL = "https://s3-us-west-2.amazonaws.com/"+bucketName+"/" + key;
			return URL;
	}

}
