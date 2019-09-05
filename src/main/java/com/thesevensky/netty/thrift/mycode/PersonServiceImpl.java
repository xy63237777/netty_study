package com.thesevensky.netty.thrift.mycode;

import com.thesevensky.netty.thrift.generated.DataException;
import com.thesevensky.netty.thrift.generated.Person;
import com.thesevensky.netty.thrift.generated.PersonService;

public class PersonServiceImpl implements PersonService.Iface {

    @Override
    public Person getPersonByName(String name) throws DataException, org.apache.thrift.TException {
        System.out.println("getPersonByName call....");
        Person person = new Person();

        person.setName("刘亦菲");
        person.setAge(20);
        person.setMarried(false);

        return person;
    }

    @Override
    public void savePerson(Person person) throws DataException, org.apache.thrift.TException {
        System.out.println("savePerson call....");
        System.out.println(person.getName());
        System.out.println(person.getAge());
        System.out.println(person.isMarried());
    }
}
