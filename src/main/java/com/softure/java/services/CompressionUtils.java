package com.softure.java.services;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class CompressionUtils {

	public static byte[] compressZip(byte[] data) throws IOException {
		Deflater deflater = new Deflater();  
		deflater.setInput(data);  
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);   
		deflater.finish();  
		byte[] buffer = new byte[1024];   
		while (!deflater.finished()) {  
			int count = deflater.deflate(buffer); // returns the generated code... index  
			outputStream.write(buffer, 0, count);   
		}  
		outputStream.close();  
		byte[] output = outputStream.toByteArray();  
		return output;  
	}  	
	
	public static byte[] decompressZip(byte[] data) throws IOException, DataFormatException {  
		Inflater inflater = new Inflater();   
		inflater.setInput(data);  
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);  
		byte[] buffer = new byte[1024];  
		while (!inflater.finished()) {  
			int count = inflater.inflate(buffer);  
			outputStream.write(buffer, 0, count);  
		}  
		outputStream.close();  
		byte[] output = outputStream.toByteArray();  
		return output;  
	}
	
	public static byte[] compress(byte[] data) throws IOException {
		
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		BufferedImage bufferedImage = ImageIO.read(bais);
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	    //ImageWriter writer = ImageIO.getImageWritersByFormatName(imageType.getExtension()).next();
	    ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.5f);
        }

        writer.write(null, new IIOImage(bufferedImage, null, null), param);
        writer.dispose();
        return outputStream.toByteArray();

	    
/*		
		File input = new File("digital_image_processing.jpg");
	    BufferedImage image = ImageIO.read(input);

	    File compressedImageFile = new File("compress.jpg");
	    OutputStream os =new FileOutputStream(compressedImageFile);

	    Iterator<ImageWriter>writers =  ImageIO.getImageWritersByFormatName("jpg");
	    ImageWriter writer = (ImageWriter) writers.next();

	    ImageOutputStream ios = ImageIO.createImageOutputStream(os);
	    writer.setOutput(ios);

	    ImageWriteParam param = writer.getDefaultWriteParam();
	    
	    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	    param.setCompressionQuality(0.5f);
	    writer.write(null, new IIOImage(image, null, null), param);
	    
	    os.close();
	    ios.close();
	    writer.dispose();
	    */
	}
}

