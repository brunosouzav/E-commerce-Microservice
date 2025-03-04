package com.user.microservice.service;

import com.user.microservice.domains.User;
import com.user.microservice.domains.VerificationCode;
import com.user.microservice.dto.UserResponseCode;
import com.user.microservice.exceptions.CodeInvalidException;
import com.user.microservice.exceptions.UserAlreadyExistsException;
import com.user.microservice.exceptions.UserInvalidException;
import com.user.microservice.repositories.UserRepository;
import com.user.microservice.repositories.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

   @Autowired
   private UserEventService userEventService;


    public String register(User user) {
       User existUser = userRepository.findByEmail(user.getEmail());

        if (existUser != null && existUser.getEmail().equals(user.getEmail())) {
            throw new UserAlreadyExistsException("Email já existente");

        }

        userRepository.save(user);

        VerificationCode verificationCode = new VerificationCode(user);
        verificationCode.generateVerificationCode();
        verificationCodeRepository.save(verificationCode);

        UserResponseCode userResponseCode = new UserResponseCode(user.getName(), user.getEmail(), verificationCode.getCode());
        userEventService.enfileirarRegistros(userResponseCode);

        return "Foi enviado um código de confirmação para o email " + user.getEmail() + " " + verificationCode.getCode();

    }


    public String validCode(int code) {
        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findByCode(code);

        if (verificationCodeOptional.isEmpty()) {
            throw new CodeInvalidException("Código inválido.") ;
        }

        VerificationCode verificationCode = verificationCodeOptional.get();

        if (verificationCode.isExpired()) {
            throw new CodeInvalidException("Código expirado. Solicite um novo.");
        }

        User user = verificationCode.getUser();
        user.setStatus(true);
        userRepository.save(user);

        verificationCodeRepository.delete(verificationCode);

        return "Código válido! Conta ativada com sucesso.";
    }

    public List<User> listAllUser(){
        return userRepository.findAll();
    }

    public String login(String email) {
        User existUser = userRepository.findByEmail(email);

        if(existUser == null) {
            throw new UserInvalidException("Email não foi cadastrado");
        }


        VerificationCode verificationCode = new VerificationCode(existUser);
        verificationCode.generateVerificationCode();
        verificationCodeRepository.save(verificationCode);

        userRepository.save(existUser);

        UserResponseCode userResponseCode = new UserResponseCode(existUser.getName(), existUser.getEmail(), verificationCode.getCode());
        userEventService.enfileirarRegistros(userResponseCode);

        return  "Codigo de verificação enviado para o seu email " ;
    }

}