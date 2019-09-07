package com.thesevensky.netty.gRPC;

import com.thesevensky.proto.*;
import io.grpc.stub.StreamObserver;

import java.util.UUID;

public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {

    @Override
    public void getRealNameByUsername(MyRequest request, StreamObserver<MyResponse> responseObserver) {
        System.out.println("接受到客户端信息: " + request.getUsername());
        responseObserver.onNext(MyResponse.newBuilder().setRealName("刘亦菲").build());
        responseObserver.onCompleted();
    }

    @Override
    public void getStudentsByAge(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        System.out.println("getStudentsByAge call.... " + request.getAge());

        responseObserver.onNext(StudentResponse.newBuilder().setName("刘亦菲")
                .setAge(18).setCity("杭州").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("唐嫣")
                .setAge(18).setCity("北京").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("杨颖")
                .setAge(18).setCity("上海").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("关羽")
                .setAge(18).setCity("蜀国").build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<StudentRequest> getStudentsWrapperByAges(StreamObserver<StudentResponseList> responseObserver) {
        //事件回调 如果客户端产生相应的事件 则会调用下面的方法
        return new StreamObserver<StudentRequest>() {
            @Override
            public void onNext(StudentRequest studentRequest) {
                System.out.println("onNext --- " + studentRequest.getAge());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(StudentResponseList.newBuilder()
                        .addStudentResponse(
                                StudentResponse.newBuilder().setName("马超").setCity("蜀国").setAge(18).build())
                        .addStudentResponse(
                                StudentResponse.newBuilder().setName("关羽").setCity("蜀国").setAge(19).build())
                        .build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<StreamRequest> biTalk(StreamObserver<StreamResponse> responseObserver) {
        return new StreamObserver<StreamRequest>() {
            @Override
            public void onNext(StreamRequest streamRequest) {
                System.out.println("onNext --- " + streamRequest.getRequestInfo());

                responseObserver.onNext(StreamResponse.newBuilder()
                        .setResponseInfo(UUID.randomUUID().toString()).build());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("server onCompleted call....");
                responseObserver.onCompleted();
            }
        };
    }
}
