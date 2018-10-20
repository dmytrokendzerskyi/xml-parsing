package com.parsing;

import com.sun.xml.internal.bind.v2.util.XmlFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.*;
import javax.xml.parsers.*;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String FILE_PATH = "user.xml";

    public static void main(String[] args) throws JAXBException, ParserConfigurationException, IOException, SAXException, XMLStreamException {

        InputStream inputStream = new FileInputStream(FILE_PATH);
        
          parseWithDOM(reOpenInputStream(inputStream));
          parseWithSAX(reOpenInputStream(inputStream));
          parseWithSTAX(reOpenInputStream(inputStream));
          parseWithJAXB(reOpenInputStream(inputStream));
    }

    private static void parseWithJAXB(InputStream inputStream) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Users users = (Users) unmarshaller.unmarshal(inputStream);
        users.getUser().forEach(System.out::println);
    }


    private static void parseWithSTAX(InputStream inputStream) throws XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);
        Users users = new Users();
        Users.User user = null;
        Users.User.Roles.Role role = null;
        Users.User.Roles roles = null;
        List<Users.User> userList = new ArrayList<>();
        boolean bRole = false;
        while (xmlEventReader.hasNext()){
            XMLEvent xmlEvent = xmlEventReader.nextEvent();
            if(xmlEvent.isStartElement()){
                StartElement startElement = xmlEvent.asStartElement();
                if(startElement.getName().getLocalPart().equals("user")){
                    user = new Users.User();
                }else if(startElement.getName().getLocalPart().equals("id")){
                    xmlEvent = xmlEventReader.nextEvent();
                    if(bRole){
                        role.setId(Integer.valueOf(xmlEvent.asCharacters().getData()));
                    }else {
                        user.setId(Integer.valueOf(xmlEvent.asCharacters().getData()));
                    }
                }else if(startElement.getName().getLocalPart().equals("name")){
                    xmlEvent = xmlEventReader.nextEvent();
                    if(bRole){
                        role.setName(xmlEvent.asCharacters().getData());
                    }else {
                        user.setName(xmlEvent.asCharacters().getData());
                    }
                }else if(startElement.getName().getLocalPart().equals("age")){
                    xmlEvent = xmlEventReader.nextEvent();
                    user.setAge(Integer.valueOf(xmlEvent.asCharacters().getData()));
                }else if(startElement.getName().getLocalPart().equals("active")){
                    xmlEvent = xmlEventReader.nextEvent();
                    user.setActive(Boolean.getBoolean(xmlEvent.asCharacters().getData()));
                }else if(startElement.getName().getLocalPart().equals("gender")){
                    xmlEvent = xmlEventReader.nextEvent();
                    user.setGender(Gender.fromValue(xmlEvent.asCharacters().getData()));
                }else if(startElement.getName().getLocalPart().equals("role")){
                    role = new Users.User.Roles.Role();
                    bRole = true;
                }else if(startElement.getName().getLocalPart().equals("roles")){
                    roles = new Users.User.Roles();
                    roles.role = new ArrayList<>();
                }
            }

            if(xmlEvent.isEndElement()){
                EndElement endElement = xmlEvent.asEndElement();
                if(endElement.getName().getLocalPart().equals("user")){
                    user.roles = roles;
                    userList.add(user);
                } else if(endElement.getName().getLocalPart().equals("role")){
                    roles.getRole().add(role);
                    bRole = false;
                }
            }
        }
        users.user = userList;
        users.getUser().forEach(System.out::println);
    }

    private static void parseWithSAX(InputStream fileXmlInputStream) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        UsersHadler usersHadler = new UsersHadler();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        saxParser.parse(fileXmlInputStream, usersHadler);
        Users users = new Users();
        users.user = usersHadler.getUserList();
        users.getUser().forEach(System.out::println);
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
