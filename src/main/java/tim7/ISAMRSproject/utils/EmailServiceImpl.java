package tim7.ISAMRSproject.utils;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import tim7.ISAMRSproject.model.Reservation;
import tim7.ISAMRSproject.model.User;

@Service
public class EmailServiceImpl {

	@Autowired
	private JavaMailSender mailSender;
	
	private String CONFIRMATION_SUBJECT = "GoFishing! Account Verification";
	private String RESERVATION_CONFIRMATION_SUBJECT = "GoFishing! Reservation Confirmation";
	private String ACTION_SUBJECT = "Go Fishing AKCIJA!!!";

	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("gofishingteam7@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		mailSender.send(message);
		System.out.println("Email sent successfully!");
	}
	
	public void sendConfirmationMail(User user) {
		String confirmationBody = "Dear " + user.getName() + " " + user.getLastName() + ", \n";
		confirmationBody += "Thank you for joining Go Fishing! In order to activate your account, ";
		confirmationBody += "please visit the following link: http://localhost:8080/reg/activate/" + user.getId().toString();
		sendSimpleMessage(user.getEmail(), CONFIRMATION_SUBJECT, confirmationBody);
	}

	public void sendActionEmail(User user, Reservation action){
		String confirmationBody = "Poštovani, " + user.getName() + " " + user.getLastName() + ", \n";
		confirmationBody += "Imamo novu akciju za Vas!\n";
		confirmationBody += "\nU pitanju je ponuda: " + action.getOffer().getName();
		confirmationBody += "\nPočetak: " + action.getStartDateTime().toString();
		confirmationBody += "\nKraj: " + action.getEndDateTime().toString();
		confirmationBody += "\nCena: " + action.getTotalPrice();

		sendSimpleMessage(user.getEmail(), ACTION_SUBJECT, confirmationBody);
	}
	
	public void sendAdminConfirmationMail(User user) {
		String confirmationBody = "Dear " + user.getName() + " " + user.getLastName() + ", \n";
		confirmationBody += "Thank you for joining Go Fishing! You have been added as admin! ";
		
		sendSimpleMessage("djordjejovanovic27@gmail.com", "Go fishing admin", confirmationBody);
	}
	
	public void sendDeletionEmail(User user, boolean deleted, String reason) {
		String confirmationBody = "Dear " + user.getName() + " " + user.getLastName() + ", \n";
		if (deleted) {
			confirmationBody += "Your account on GoFishing! has been successfully deleted!\n";
		}
		else {
			confirmationBody += "Your request for deletion of your account has been refused!\n";
			confirmationBody += "Reason for refusal: " + reason + "\n";
		}
		confirmationBody += "\nGoFishing! admin team";
		sendSimpleMessage("djordjejovanovic27@gmail.com", "Go fishing admin", confirmationBody);
	}
	
	public void sendRegistrationEmail(User user, boolean registered, String reason) {
		String confirmationBody = "Dear " + user.getName() + " " + user.getLastName() + ", \n";
		if (registered) {
			confirmationBody += "Your account on GoFishing! has been successfully added!\n";
			confirmationBody += "You can now log in with your email and password!\n";
		}
		else {
			confirmationBody += "Your request for registration has been refused!\n";
			confirmationBody += "Reason for refusal: " + reason + "\n";
		}
		confirmationBody += "\nGoFishing! admin team";
		sendSimpleMessage("djordjejovanovic27@gmail.com", "Go fishing admin", confirmationBody);
	}
	
	public void sendComplaintResponse(User accuser, User offender, String response) {
		System.out.println(accuser.getEmail());
		System.out.println(offender.getEmail());
		String confirmationBodyAccuser = "Dear " + accuser.getName() + " " + accuser.getLastName() + ", \n";	
		confirmationBodyAccuser += "Complaint that you have filed against " + offender.getName() + " " + offender.getLastName();
		confirmationBodyAccuser += " has reached out admin team and it has been accepted.";
		confirmationBodyAccuser += "\nAdmin's response: " + response;
		sendSimpleMessage(accuser.getEmail(), "Go fishing admin", confirmationBodyAccuser);
		
		String confirmationBodyOffender = "Dear " + offender.getName() + " " + offender.getLastName() + ", \n";	
		confirmationBodyOffender += "User " + accuser.getName() + " " + accuser.getLastName() + " has filed complaint against you.\n";
		confirmationBodyOffender += "Complaint has been accepted!";
		confirmationBodyOffender += "\nAdmin's response: " + response;
		sendSimpleMessage(offender.getEmail(), "Go fishing admin", confirmationBodyOffender);
  }
  
	public void sendReservationConfirmationMail(User user, Reservation res, String offerName) {
		String confirmationBody = "Dear " + user.getName() + " " + user.getLastName() + ",\n";
		confirmationBody += "You have successfully made a reservation for " + offerName + "!\n";
		confirmationBody += "Start date: " + res.getStartDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))  + "\n";
		confirmationBody += "End date: " + res.getEndDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))  + "\n";
		confirmationBody += "GoFishing! Team wishes you a pleasant stay!";
		sendSimpleMessage(user.getEmail(), RESERVATION_CONFIRMATION_SUBJECT, confirmationBody);
	}
	
	public void sendReviewAcceptedMail(User user, String reviewText, Float grade) {
		String confirmationBody = "Dear " + user.getName() + " " + user.getLastName() + ",\n";
		confirmationBody += "New review for one of your services has been approved!\n";
		confirmationBody += "This is what client had to say about your service: \n";
		confirmationBody += "\tReview: " + reviewText + "\n";
		confirmationBody += "\tGrade: " + grade + "/5 \n";
		confirmationBody += "GoFishing! Team wishes you all the best!";
		sendSimpleMessage(user.getEmail(), "New review for your service!", confirmationBody);
	}
}
