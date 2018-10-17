package com.parsing;

import javafx.scene.input.KeyCode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class UsersHadler extends DefaultHandler {


    private Users.User user;
    private List<Users.User> userList = new ArrayList<>();

    protected boolean bid;
    protected boolean bname;
    protected boolean bage;
    protected boolean bgender;
    protected boolean bactive;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if(qName.equalsIgnoreCase("user")){
            user = new Users.User();
        }else if(qName.equalsIgnoreCase("id")){
            bid = true;
        } else if(qName.equalsIgnoreCase("name")){
            bname = true;
        } else if(qName.equalsIgnoreCase("age")){
            bage = true;
        } else if(qName.equalsIgnoreCase("gender")){
            bgender = true;
        } else if(qName.equalsIgnoreCase("active")) {
            bactive = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equalsIgnoreCase("user")){
            userList.add(user);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(bid){
            user.setId(Integer.valueOf(new String(ch,start,length)));
            bid = false;
        } else if(bname){
            user.setName(new String(ch,start,length));
            bname = false;
        }else if(bage){
            user.setAge(Integer.valueOf(new String(ch,start,length)));
            bage = false;
        }else if(bgender){
            user.setGender(Gender.fromValue(new String(ch,start,length)));
            bgender = false;
        }else if(bactive){
            user.setActive(Boolean.getBoolean(new String(ch,start,length)));
            bactive = false;
        }
    }

    public List<Users.User> getUserList() {
        return userList;
    }

    public void setUserList(List<Users.User> userList) {
        this.userList = userList;
    }
}
