package alex.msu.library.converters;

import alex.msu.library.commands.ActorCommand;
import alex.msu.library.domain.Actor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;



@Component
public class ActorCommandToActor implements Converter<ActorCommand, Actor> {

    @Synchronized
    @Nullable
    @Override
    public Actor convert(ActorCommand source) {
        if (source == null){
            return null;
        }

        final Actor actor = new Actor();
        actor.setId(source.getId());
        actor.setName(source.getName());
        actor.setPatronymic(source.getPatronymic());
        actor.setSurname(source.getSurname());
        actor.setMemo(source.getMemo());

        return actor;

    }
}
