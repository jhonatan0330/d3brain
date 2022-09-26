package com.softure.java.services;


import java.awt.Graphics2D;
import java.awt.RenderingHints;
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

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;

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
	
	private static BufferedImage rotateImage(BufferedImage buffImage, double angle) {
		if (angle ==0) return buffImage;
		double radian = Math.toRadians(angle);

	    double sin = Math.abs(Math.sin(radian));
	    double cos = Math.abs(Math.cos(radian));

	    int width = buffImage.getWidth();
	    int height = buffImage.getHeight();

	    int nWidth = (int) Math.floor((double) width * cos + (double) height * sin);
	    int nHeight = (int) Math.floor((double) height * cos + (double) width * sin);

	    BufferedImage rotatedImage = new BufferedImage(
	            nWidth, nHeight, buffImage.getType());

	    Graphics2D graphics = rotatedImage.createGraphics();

	    graphics.setRenderingHint(
	            RenderingHints.KEY_INTERPOLATION,
	            RenderingHints.VALUE_INTERPOLATION_BICUBIC);

	    graphics.translate((nWidth - width) / 2, (nHeight - height) / 2);
	    // rotation around the center point
	    graphics.rotate(radian, (double) (width / 2), (double) (height / 2));
	    graphics.drawImage(buffImage, 0, 0, null);
	    graphics.dispose();
	    
	    return rotatedImage;
/*
	    AffineTransform transform = new AffineTransform();
	    transform.rotate(radian, buffImage.getWidth() / 2, buffImage.getHeight() / 2);
	    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
	    return op.filter(buffImage, null);
	*/    
	}
	
	private static double getRotation(byte[] data) {
		int orientation = 0;
		try {
			final Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(data));
    	    final ExifIFD0Directory exifDirectory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
    	    if(exifDirectory!=null && exifDirectory.containsTag(ExifIFD0Directory.TAG_ORIENTATION) )orientation = exifDirectory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
		} catch (ImageProcessingException |MetadataException | IOException e) {
			e.printStackTrace();
		} 
		System.out.println("Orientation" +orientation);
		if ( orientation == 2 ) {
			//return rotateBitmap(img.getBitmap(), ImageUtil.FLIP_H);
			return 0;
		} else if ( orientation == 3 ) {
			return 180;
		} else if ( orientation == 4 ) {
			// return rotateBitmap(img.getBitmap(), ImageUtil.FLIP_V);
			return 0;
		} else if ( orientation == 5 ) {
			// Bitmap tmp = rotateBitmap(img.getBitmap(), ImageUtil.FLIP_H);
			// tmp = rotateBitmap(tmp, ImageUtil.FLIP_90CCW);
			// return tmp;
			return 0;
		} else if ( orientation == 6 ) {
			return 90;
		} else if ( orientation == 7 ) {
			//Bitmap tmp = rotateBitmap(img.getBitmap(), ImageUtil.FLIP_H);
			//tmp = rotateBitmap(tmp, ImageUtil.FLIP_90CW);
			//return tmp;
			return 0;
		} else if ( orientation == 8 ) {
			return 270;
		} else {
			return 0;
		}
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

        writer.write(null, new IIOImage(rotateImage(bufferedImage,getRotation(data)), null, null), param);
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

