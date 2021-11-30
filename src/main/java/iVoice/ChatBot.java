package iVoice;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;


public class ChatBot {
    public BufferedReader br;
    private final ActorContext<Command> ctx;

    public ChatBot(ActorContext<Command> ctx) {
        this.ctx = ctx;
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    interface Command{}

    enum Wait implements Command{
        INSTANCE
    }

    enum Answer implements  Command {
        INSTANCE
    }

    private Behavior<Command> waiting() throws IOException {
        String question  = "";
        String finalQuestion = question;
        TimerTask task = new TimerTask()
        {
            public void run()
            {
                if( finalQuestion.equals("") )
                {
                    System.out.println("I got bored");
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule( task, 2*1000);
        System.out.println("Ask a question:");
        question = br.readLine();
        timer.cancel();
        System.out.println("So, you have asked "+ question);

        return Behaviors.receive(Command.class)
                .onMessage(Answer.class, msg -> {
                    return startAnswering(Duration.ofSeconds(1)); })
                .build();
    }
    private Behavior<Command> answering() {
        return Behaviors.receive(Command.class)
                .onMessage(Wait.class, msg -> {
                    System.out.println("Actually, I don't know");
                    return startWaiting(Duration.ofSeconds(0));
                }).build();
    }
    private Behavior<Command> gettingBored() {
        return Behaviors.receive(Command.class)
                .onMessage(Wait.class, msg -> {
                    System.out.println("I got bored");
                    return startWaiting(Duration.ofSeconds(0));
                }).build();
    }
    public static Behavior<Command> create() {
        return Behaviors.setup(ctx -> new ChatBot(ctx).waiting());
    }
    private Behavior<Command> startGettingBored(Duration duration) {
        ctx.scheduleOnce(duration, ctx.getSelf(), Wait.INSTANCE);
        return gettingBored();
    }
    private Behavior<Command> startAnswering(Duration duration) {
        ctx.scheduleOnce(duration, ctx.getSelf(), Wait.INSTANCE);
        return answering();
    }
    private Behavior<Command> startWaiting(Duration duration) throws IOException {
        ctx.scheduleOnce(duration, ctx.getSelf(), Answer.INSTANCE);
        return waiting();
    }
}
