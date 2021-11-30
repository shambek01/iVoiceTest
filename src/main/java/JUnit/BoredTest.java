package JUnit;

import akka.NotUsed;
import akka.actor.typed.ActorSystem;
import iVoice.ChatClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import akka.actor.typed.javadsl.ActorContext;
import akka.NotUsed;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BoredTest {

    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(output));
    }

    @Test
    public void testString() throws InterruptedException {
        Thread.sleep(1100);
        ActorSystem.create(ChatClient.create(), "ChatClient");
        Assert.assertEquals("I got bored", output.toString());
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
    }
}