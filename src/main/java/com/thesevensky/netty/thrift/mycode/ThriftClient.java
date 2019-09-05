package com.thesevensky.netty.thrift.mycode;

import com.thesevensky.netty.thrift.generated.Person;
import com.thesevensky.netty.thrift.generated.PersonService;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class ThriftClient {
    public static void main(String[] args) {
        TTransport tTransport = new TFramedTransport(new TSocket("localhost", 8899), 600);
        TProtocol protocol = new TCompactProtocol(tTransport);
        PersonService.Client client = new PersonService.Client(protocol);
        try{
            tTransport.open();

            Person person = client.getPersonByName("刘亦菲");
            System.out.println(person.getName());
            System.out.println(person.getAge());

            Person person1 = new Person();
            person1.setName("唐嫣");
            person1.setAge(21);
            person1.setMarried(false);
            client.savePerson(person1);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            tTransport.close();
        }
    }
}
