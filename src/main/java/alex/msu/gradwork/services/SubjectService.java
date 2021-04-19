package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.NoteCommand;
import alex.msu.gradwork.commands.SubjectCommand;

import java.util.Set;

public interface SubjectService {

    SubjectCommand findByNoteIdAndSubjectId(Long noteId, Long subjectId);

    Set<NoteCommand> findAllNoteBySubjectId (Long l);

//    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);
//
    SubjectCommand saveSubjectCommand(SubjectCommand command);

//    void deleteById(Long recipeId, Long idToDelete);
}
