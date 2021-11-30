package iVoice;

import akka.NotUsed;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;

public class ChatClient {
    private ChatClient(ActorContext<NotUsed> context) {
        this.context = context;
    }

    public static void main(String[] args) {
        ActorSystem.create(ChatClient.create(), "ChatClient");
    }

    public static Behavior<NotUsed> create() {
        return Behaviors.setup(context -> new ChatClient(context).behavior());
    }

    private final ActorContext<NotUsed> context;

    private Behavior<NotUsed> behavior() {
        context.spawn(ChatBot.create(),"ChatBot").tell(ChatBot.Wait.INSTANCE);
        return Behaviors.empty();
    }
}

