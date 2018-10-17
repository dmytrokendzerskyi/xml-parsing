package com.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String FILE_PATH = "user.xml";

    public static void main(String[] args) throws JAXBException,ParserConfigurationException,IOException,SAXException {

        JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
//        OutputStream outputStream = new FileOutputStream(FILE_PATH);
//        List<User.Role> roles = new ArrayList<>();
//        roles.add(new User.Role(1L,"ADMIN"));
//        roles.add(new User.Role(2L,"USER"));
//        User user = new User(1L,"James Gosling",60, User.Gender.MALE, "programing", roles );
//        marshaller.marshal(user,outputStream);
        InputStream inputStream = new FileInputStream(FILE_PATH);
//        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//        Users users = (Users) unmarshaller.unmarshal(inputStream);
//        users.getUser().forEach(System.out::println);

   //     parseWithDOM(reOpenInputStream(inputStream));
        parseWithSAX(reOpenInputStream(inputStream));
    }

    private static void parseWithSAX(InputStream fileXmlInputStream) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        UsersHadler usersHadler = new UsersHadler();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        saxParser.parse(fileXmlInputStream, usersHadler);
        Users users = new Users();
        users.user = usersHadler.getUserList();
    }

    private static void parseWithDOM(InputStream fileXmlInputStream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(fileXmlInputStream);
        Users users = new Users();
        List<Users.User> userList = new ArrayList<>();

        NodeList nodeList = document.getElementsByTagName("user");
        for (int i = 0; i< nodeList.getLength(); i++) {
            Users.User user = new Users.User();
            Element elementUser = (Element) nodeList.item(i);
            user.setId(Integer.valueOf(elementUser.getElementsByTagName("id").item(0).getTextContent()));
            user.setName(elementUser.getElementsByTagName("name").item(0).getTextContent());
            user.setAge(Integer.valueOf(elementUser.getElementsByTagName("age").item(0).getTextContent()));
            user.setActive(Boolean.getBoolean(elementUser.getElementsByTagName("active").item(0).getTextContent()));
            user.setGender(Gender.fromValue(elementUser.getElementsByTagName("gender").item(0).getTextContent()));
            Users.User.Roles roles = new Users.User.Roles();
            List<Users.User.Roles.Role> roleList = new ArrayList<>();
            NodeList roleNodes = elementUser.getElementsByTagName("role");
            for (int j = 0; j < roleNodes.getLength(); j++) {
                Users.User.Roles.Role role = new Users.User.Roles.Role();
                Element elementRole  =  (Element) roleNodes.item(j);
                role.setId(Integer.valueOf(elementRole.getElementsByTagName("id").item(0).getTextContent()));
                role.setName(elementRole.getElementsByTagName("name").item(0).getTextContent());
                roleList.add(role);
            }
            roles.role = roleList;
            user.setRoles(roles);
            userList.add(user);
        }
        users.user = userList;
        users.getUser().forEach(System.out::println);

    }

    public static InputStream reOpenInputStream(InputStream inputStream) throws IOException {
        inputStream.close();
        return new FileInputStream(FILE_PATH);
    }
}
