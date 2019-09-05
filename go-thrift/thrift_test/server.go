package main

import (
	"context"
	"fmt"
	"github.com/apache/thrift/lib/go/thrift"
	"log"
	"newstudy/thrift_test/generated"
)

type rpcServiceImpl struct {

}

func (this *rpcServiceImpl) GetPersonByName(ctx context.Context,name string) (*generated.Person,error)  {
	fmt.Println("golang getPersonByName call.... ")
	person := generated.NewPerson()
	str := "伏羲"
	person.Name = &str
	var age generated.Int = 99999
	person.Age = &age
	flag := false
	person.Married = &flag
	fmt.Println("getPersonByName return....")
	return person, nil
}

func (this *rpcServiceImpl) SavePerson(ctx context.Context, person *generated.Person) error {
	fmt.Println("golang savePerson call....")
	fmt.Println(*person.Name)
	fmt.Println(*person.Age)
	return nil
}

func checkErr(err error,msg string)  {
	if err != nil {
		log.Fatalln(msg, "  " , err)
	}
}

func main() {
	serverSocket, e := thrift.NewTServerSocket("localhost:8899")
	checkErr(e,"开始连接")
	transportFactory := thrift.NewTFramedTransportFactory(thrift.NewTTransportFactory())
	protocolFactory := thrift.NewTCompactProtocolFactory()
	handler := &rpcServiceImpl{}
	//注释的部分是多接口的 可以上网查一下多接口的调用
	//multiplexedProcessor := thrift.NewTMultiplexedProcessor()

	processor := generated.NewPersonServiceProcessor(handler)
	//multiplexedProcessor.RegisterProcessor("rpc",processor)
	server := thrift.NewTSimpleServer4(processor, serverSocket, transportFactory, protocolFactory)
	e = server.Serve()
	checkErr(e,"服务启动失败")
}