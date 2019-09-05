package main

import (
	"context"
	"fmt"
	"github.com/apache/thrift/lib/go/thrift"
	"log"
	"net"
	ini "newstudy/thrift_test/generated"
)

type serverImpl struct {

}


var defaultCtx = context.Background()

func checkError(err error, msg string)  {
	if err != nil {
		log.Fatalln(msg, err)
	}
}

func main() {

	transportFactory := thrift.NewTFramedTransportFactory(thrift.NewTTransportFactory())
	protocolFactory := thrift.NewTCompactProtocolFactory()
	transport, e := thrift.NewTSocket(net.JoinHostPort("127.0.0.1", "8899"))
	checkError(e,"port")
	useTransport, err := transportFactory.GetTransport(transport)
	checkError(err,"useTransport")
	client := ini.NewPersonServiceClientFactory(useTransport, protocolFactory)
	e = transport.Open()
	checkError(e, "Open ")
	defer transport.Close()
	r, e := client.GetPersonByName(defaultCtx,"刘亦菲")
	checkError(e,"GetPersonByName")
	fmt.Println(*r.Name)
	fmt.Println(*r.Age)

	var person ini.Person
	flag := false
	person.Married = &flag
	str := "唐嫣"
	person.Name = &str
	client.SavePerson(defaultCtx, &person)
}


