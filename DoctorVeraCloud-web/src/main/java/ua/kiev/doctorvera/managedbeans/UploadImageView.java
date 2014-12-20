package ua.kiev.doctorvera.managedbeans;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
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
import ua.kiev.doctorvera.web.resources.Message;

@ManagedBean(name="uploadImageView")
@SessionScoped
public class UploadImageView {
	
	@EJB
	private UsersFacadeLocal usersFacade;
	
    @ManagedProperty(value="#{userLoginView}")
    private UserLoginView userLogin;
    
    @ManagedProperty(value="#{userProfileView}")
    UserProfileView userProfile;
    
	private CroppedImage croppedImage;

	private String tempImageName;
	
	private String userId;
	
	private final static Logger LOG = Logger.getLogger(UploadImageView.class.getName());
	private final String AVATAR_IMAGES_PATH = Mapping.getInstance().getProperty(Mapping.Path.APPLICATION_AVATAR_IMAGES_PATH);
	private final String SUCCESS_MESSAGE = Message.getInstance().getMessage(Message.UsersDetails.PROFILE_CROP_AVATAR_SUCCESS_MESSAGE);
	private final String SUCCESS_TITLE = Message.getInstance().getMessage(Message.UsersDetails.PROFILE_CROP_AVATAR_SUCCESS_TITLE);
	private final String ERROR_MESSAGE = Message.getInstance().getMessage(Message.UsersDetails.PROFILE_CROP_AVATAR_ERROR_MESSAGE);
	private final String ERROR_TITLE = Message.getInstance().getMessage(Message.UsersDetails.PROFILE_CROP_AVATAR_ERROR_TITLE);

	public UploadImageView(){}
	
	@PostConstruct
	public void init(){
		LOG.info(userLogin.getClass() +" injected into" + this.getClass() + "!");
		LOG.info(userProfile.getClass() +" injected into"+ this.getClass() + "!");
	}
	
	public void setUserLogin(UserLoginView userLogin) {
		this.userLogin = userLogin;
	}
	
	public void setUserProfile(UserProfileView userProfile) {
		this.userProfile = userProfile;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
    	LOG.info("Time " + new Date().toString() + " Starting to upload avatar:");
		if (file != null && userId != null) {
			try{
				final ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
				final InputStream input = file.getInputstream();
				final String filename = "temp_avatar_" + userId + ".";
				final String extension	 = FilenameUtils.getExtension(file.getFileName());
				final String path = servletContext.getRealPath("") + File.separator + AVATAR_IMAGES_PATH;
					
				tempImageName = filename + extension;
				LOG.info("Avatar temp path: " + path + tempImageName );
				
				File tempFile = new File(path + filename + extension);
				tempFile.createNewFile();
				Files.copy(input, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				resizeImage(tempFile,extension);
				
				LOG.info("Avatar successfuly uploaded");
			}catch(Exception e){
				LOG.log(Level.SEVERE, "Exception: ", e);
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
	    LOG.info("Avatar image resized");
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
			LOG.log(Level.SEVERE, "Exception: ", e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_TITLE, ERROR_MESSAGE));
		}
		
		LOG.info("Avatar image Cropped");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, SUCCESS_TITLE, SUCCESS_MESSAGE));
		if(userId.equals(userLogin.getAuthorizedUser().getId().toString()))userLogin.refresh();
		userProfile.refresh();
		RequestContext.getCurrentInstance().closeDialog(null);
	}


}
