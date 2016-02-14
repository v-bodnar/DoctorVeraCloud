package ua.kiev.doctorvera.views;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.UploadedFile;
import ua.kiev.doctorvera.entities.Users;
import ua.kiev.doctorvera.facadeLocal.UsersFacadeLocal;
import ua.kiev.doctorvera.resources.Mapping;
import ua.kiev.doctorvera.resources.Message;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named(value="uploadImageView")
@SessionScoped
public class UploadImageView implements Serializable {

	@EJB
	private UsersFacadeLocal usersFacade;

	@Inject
	private UserLoginView userLogin;

	@Inject
	private UserProfileView userProfile;

	@Inject
	private SessionParams sessionParams;

	private Users authorizedUser;

	private CroppedImage croppedImage;

	private String userId;

	private String tempImageName;

	private final static Logger LOG = Logger.getLogger(UploadImageView.class.getName());
	private final String AVATAR_IMAGES_PATH = Mapping.getInstance().getString("APPLICATION_AVATAR_IMAGES_PATH");
	private final String SUCCESS_MESSAGE = Message.getMessage("PROFILE_CROP_AVATAR_SUCCESS_MESSAGE");
	private final String SUCCESS_TITLE = Message.getMessage("PROFILE_CROP_AVATAR_SUCCESS_TITLE");
	private final String ERROR_MESSAGE = Message.getMessage("PROFILE_CROP_AVATAR_ERROR_MESSAGE");
	private final String ERROR_TITLE = Message.getMessage("PROFILE_CROP_AVATAR_ERROR_TITLE");

	public UploadImageView(){}

	@PostConstruct
	public void init(){
		authorizedUser = sessionParams.getAuthorizedUser();
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

	public String getTempImageName() {
		return tempImageName;
	}

	public void setTempImageName(String tempImageName) {
		this.tempImageName = tempImageName;
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

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		UploadedFile uploadedFile = event.getFile();
		LOG.info("Time " + new Date().toString() + " Starting to upload avatar:");
		if (uploadedFile != null && userId != null) {

			final InputStream input = uploadedFile.getInputstream();
			final String filename = "temp_avatar_" + userId + ".";
			final String extension	 = FilenameUtils.getExtension(uploadedFile.getFileName());
			tempImageName = filename + extension;

			File tempFile = createTempImage();
			Files.copy(input, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			resizeImage(tempFile, extension);

			LOG.info("Avatar successfuly uploaded");

		}
	}

	private File createTempImage() throws IOException {
		final ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		final String path = servletContext.getRealPath("") + File.separator + AVATAR_IMAGES_PATH;

		LOG.info("Avatar temp path: " + path + tempImageName );
		File tempFile = new File(path + tempImageName);
		tempFile.createNewFile();

		return tempFile;
	}

	private void deleteTempImage(){
		if (tempImageName == null || tempImageName.isEmpty()) return;
		final ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		final String path = servletContext.getRealPath("") + File.separator + AVATAR_IMAGES_PATH;



		File tempFile = new File(path + tempImageName);
		tempFile.delete();
		LOG.info("Avatar temp image file deleted from: " + path + tempImageName );
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
		if (croppedImage == null || userId == null || tempImageName == null) {
			return;
		}
		ByteArrayOutputStream imageOutput;
		try {
			imageOutput = new ByteArrayOutputStream();
			imageOutput.write(croppedImage.getBytes(), 0, croppedImage.getBytes().length);
			imageOutput.flush();
			imageOutput.close();

			Users currentUser = usersFacade.find(Integer.parseInt(userId));
			currentUser.setAvatarImage(imageOutput.toByteArray());
			usersFacade.edit(currentUser);
			deleteTempImage();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Exception: ", e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_TITLE, ERROR_MESSAGE));
		}

		LOG.info("Avatar image Cropped");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, SUCCESS_TITLE, SUCCESS_MESSAGE));
		if(userId.equals(authorizedUser.getId().toString()))userLogin.refresh();
		userProfile.refresh();
		RequestContext.getCurrentInstance().closeDialog(null);
	}

}
