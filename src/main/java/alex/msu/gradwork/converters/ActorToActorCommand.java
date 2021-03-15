package alex.msu.gradwork.converters;

import alex.msu.gradwork.commands.ActorCommand;
import alex.msu.gradwork.domain.Actor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class ActorToActorCommand implements Converter<Actor, ActorCommand> {
    @Override
    public ActorCommand convert(Actor actor) {
        return null;
    }
//
//    @Synchronized
//    @Nullable
//    @Override
//    public ActorCommand convert(Actor source) {
//
//        if (source == null){
//            return null;
//        }
//
//        final ActorCommand actorCommand = new ActorCommand();
//        actorCommand.setId(source.getId());
//        actorCommand.setName(source.getName());
//        actorCommand.setTitle(source.getTitle());
//        actorCommand.setType(source.getType());
//        actorCommand.setNotes(source.getNotes());
//        return actorCommand;
//
//    }
}
