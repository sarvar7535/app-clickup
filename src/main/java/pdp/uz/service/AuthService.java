package pdp.uz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pdp.uz.entity.User;
import pdp.uz.enums.SystemRoleName;
import pdp.uz.model.ApiResponse;
import pdp.uz.model.RegisterDto;
import pdp.uz.repository.UserRepository;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender javaMailSender;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public ApiResponse register(RegisterDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            return new ApiResponse("Email already exists", false);
        }
        User user = new User(
                dto.getFullName(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                SystemRoleName.SYSTEM_USER
        );
        int code = new Random().nextInt(999999);
        user.setEmailCode(String.valueOf(code).substring(0, 4));
        userRepository.save(user);
        sendEmail(user.getEmail(), user.getEmailCode());
        return new ApiResponse("Created", true, user);
    }

    public void sendEmail (String sendingEmail, String emailCode){
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("erlantest180@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Account verification");
            mailMessage.setText(emailCode);
            javaMailSender.send(mailMessage);
        }catch (Exception ignored){
        }
    }


    public ApiResponse verifyEmail(String email, String emailCode) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (emailCode.equals(user.getEmailCode())) {
                user.setEnabled(true);
                user.setEmailCode(null);
                userRepository.save(user);
                return new ApiResponse("Account is activated", true);
            }
            return new ApiResponse("Error code", false);
        }
        return new ApiResponse("No such user available", false);
    }
}
