package ua.kiev.doctorvera.managedbeans;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.UploadedFile;

import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facade.UsersFacadeLocal;
import ua.kiev.doctorvera.web.resources.Mapping;

public class UploadImageView {
	
	@EJB
	private UsersFacadeLocal usersFacade;
	
	private CroppedImage croppedImage;

	private String tempImageName;
	
	private String userId;
	
	private final String AVATAR_IMAGES_PATH = Mapping.getInstance().getProperty(Mapping.Path.APPLICATION_AVATAR_IMAGES_PATH);
	
	private Integer counter = 0;
		
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
		FacesMessage message = new FacesMessage("UserId:" + userId);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Integer getCounter() {
		return counter;
	}

	public void setCounter(Integer counter) {
		this.counter = counter;
	}

	public UploadImageView(){
		counter++;
	}
	
	public CroppedImage getCroppedImage() {
		return croppedImage;
	}

	public void setCroppedImage(CroppedImage croppedImage) {
		this.croppedImage = croppedImage;
	}
	
	public String getTempImageName() {
		return tempImageName;
	}

	public void setTempImageName(String tempImageName) {
		this.tempImageName = tempImageName;
	}
	
	
    public void handleFileUpload(FileUploadEvent event) {
    	UploadedFile file = event.getFile();
		if (file != null && userId != null) {
			try{
				final ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
				final InputStream input = file.getInputstream();
				final String filename = "temp_avatar_" + userId + ".";
				final String extension = FilenameUtils.getExtension(file.getFileName());
				final String path = servletContext.getRealPath("") + File.separator + AVATAR_IMAGES_PATH;
	
				tempImageName = filename + extension;
				
				File tempFile = new File(path + filename + extension);
				tempFile.createNewFile();
				Files.copy(input, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				resizeImage(tempFile,extension);
				FacesMessage message = new FacesMessage("Succesfuly uploaded", tempFile.getAbsolutePath() + " is uploaded.");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
    }
    
    private void resizeImage(File image, String imageType) throws IOException{
    	BufferedImage originalImage = ImageIO.read(image);
		int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		
		BufferedImage resizeImageJpg = resizeImage(originalImage, type);
		ImageIO.write(resizeImageJpg, imageType, image);
    }
    
    private static BufferedImage resizeImage(BufferedImage originalImage, int type){
    	final double aspectRatio = ((double)originalImage.getWidth())/((double)originalImage.getHeight());

    		final int IMG_HEIGHT = 300;
	    	final int IMG_WIDTH = (int)(((double)IMG_HEIGHT)*aspectRatio);
	    	BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
	    	Graphics2D g = resizedImage.createGraphics();
	    	g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
	    	g.dispose();
	    	return resizedImage;

    }

	public void crop() {
		if (croppedImage == null || userId == null) {
			return;
		}
		final String extension = FilenameUtils.getExtension(tempImageName);
		final String filename = "avatar_" + userId + "." +  extension;
		final ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		final String newFileName = servletContext.getRealPath("") + File.separator + AVATAR_IMAGES_PATH + File.separator + filename;
		
		FileImageOutputStream imageOutput;
		try {
			imageOutput = new FileImageOutputStream(new File(newFileName));
			imageOutput.write(croppedImage.getBytes(), 0, croppedImage.getBytes().length);
			imageOutput.close();
			Users currentUser = (Users)usersFacade.find(Integer.parseInt(userId));
			currentUser.setAvatarImage(filename);
			usersFacade.edit(currentUser);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cropping failed."));
		}
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfuly cropped", "Cropping finished."));
		RequestContext.getCurrentInstance().closeDialog(null);
	}

}
