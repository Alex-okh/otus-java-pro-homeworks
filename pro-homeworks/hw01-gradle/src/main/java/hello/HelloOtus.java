package hello;

import com.google.common.base.Strings;

public class HelloOtus {
    public static void main(String[] args) {
        System.out.println("Вызываем метод Strings.repeat из google guava:");
        System.out.println(Strings.repeat("123321_",10));

    }
}
