package com.one7games.protobuf;

public class ProtoBufTest {
    public static void main(String[] args) throws Exception {
        DataInfo.Student build = DataInfo.Student.newBuilder().setName("刘亦菲")
                .setAge(20).setAddress("美国").build();
        byte[] studentToByteArray = build.toByteArray();

        DataInfo.Student student = DataInfo.Student.parseFrom(studentToByteArray);
        System.out.println(student.getName());
    }
}
