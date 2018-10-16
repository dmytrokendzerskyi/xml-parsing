package com.parsing;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String FILE_PATH = "user.xml";

    public static void main(String[] args) throws JAXBException, FileNotFoundException {

        JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
//        OutputStream outputStream = new FileOutputStream(FILE_PATH);
//        List<User.Role> roles = new ArrayList<>();
//        roles.add(new User.Role(1L,"ADMIN"));
//        roles.add(new User.Role(2L,"USER"));
//        User user = new User(1L,"James Gosling",60, User.Gender.MALE, "programing", roles );
//        marshaller.marshal(user,outputStream);
        InputStream inputStream = new FileInputStream(FILE_PATH);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Users users = (Users) unmarshaller.unmarshal(inputStream);
        users.getUser().forEach(System.out::println);
    }

}
