package com.thesevensky.netty.gRPC;

import com.thesevensky.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.util.Iterator;

public class RPCClient {

    public static void main(String[] args) throws Exception {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8899)
                .usePlaintext().build();
        StudentServiceGrpc.StudentServiceBlockingStub studentServiceBlockingStub
                = StudentServiceGrpc.newBlockingStub(managedChannel);

        //异步的
        StudentServiceGrpc.StudentServiceStub stub = StudentServiceGrpc.newStub(managedChannel);
        //one request - response
        MyResponse myResponse = studentServiceBlockingStub.getRealNameByUsername(MyRequest.newBuilder()
                .setUsername("唐嫣").build());
        System.out.println(myResponse.getRealName());

        //two request - stream response
        Iterator<StudentResponse> students =
                studentServiceBlockingStub.getStudentsByAge(StudentRequest.newBuilder().setAge(123).build());
        while (students.hasNext()) {
            StudentResponse next = students.next();
            System.out.println(next.getName());
        }
        //three stream request - response
        StreamObserver<StudentResponseList> studentResponseListStreamObserver = new StreamObserver<StudentResponseList>() {
            @Override
            public void onNext(StudentResponseList studentResponseList) {
                studentResponseList.getStudentResponseList().forEach(studentResponse -> {
                    System.out.println(studentResponse.getName() + ", " + studentResponse.getCity());
                });
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted call...");
            }
        };

        StreamObserver<StudentRequest> studentRequestStreamObserver =
                stub.getStudentsWrapperByAges(studentResponseListStreamObserver);
        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(20).build());
        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(30).build());
        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(40).build());
        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(50).build());
        studentRequestStreamObserver.onCompleted();

        //这是异步发送 还没来得及发送就结束了
        Thread.sleep(1000);
        //four stream request - stream response


        StreamObserver<StreamRequest> requestStreamObserver = stub.biTalk(new StreamObserver<StreamResponse>() {
            @Override
            public void onNext(StreamResponse streamResponse) {
                System.out.println("client --- " + streamResponse.getResponseInfo());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("client onCompleted" );
            }
        });

        for (int i = 0; i < 10; i++) {
            requestStreamObserver.onNext(StreamRequest.newBuilder()
                    .setRequestInfo(LocalDateTime.now().toString()).build());
            Thread.sleep(1000);
        }
        requestStreamObserver.onCompleted();
    }
}
