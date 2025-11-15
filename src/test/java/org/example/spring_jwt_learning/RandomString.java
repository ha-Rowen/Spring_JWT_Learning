package org.example.spring_jwt_learning;
import java.util.Random;
import java.util.stream.IntStream;

public class RandomString {

    private static final Random random = new Random();
    private char[] Output;
    private int    StringMax;

    public RandomString (int StringMax)
    {
        this.Output = new char[StringMax];
        this.StringMax = StringMax;


    }

   private int RandomInt() {

       return random.nextInt(126);
   }

   private char RandomChar()
   {
     return (char)RandomInt();
   }
   private void RandomLoad ()
   {
       IntStream.range(0, this.StringMax)
               .forEach(i -> {Output[i]=RandomChar();});
   }

   public String RandomString()
   {
       RandomLoad();
       return new String(Output);
   }

}
