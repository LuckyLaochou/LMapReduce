import cn.laochou.producer.KeyProducer;

public class ProducerMain {

    public static void main(String[] args) {
        String[] targetFile = new String[] {
                "F:\\JAVA\\Base\\LMapReduce\\files\\FILE_1.txt",
                "F:\\JAVA\\Base\\LMapReduce\\files\\FILE_2.txt",
                "F:\\JAVA\\Base\\LMapReduce\\files\\FILE_3.txt",
                "F:\\JAVA\\Base\\LMapReduce\\files\\FILE_4.txt",
                "F:\\JAVA\\Base\\LMapReduce\\files\\FILE_5.txt"
        };
        KeyProducer.start(targetFile);
    }

}
