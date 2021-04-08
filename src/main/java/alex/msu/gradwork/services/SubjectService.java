package alex.msu.gradwork.services;

import alex.msu.gradwork.commands.SubjectCommand;

public interface SubjectService {

    SubjectCommand findByNoteIdAndSubjectId(Long noteId, Long subjectId);

//    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);
//
    SubjectCommand saveSubjectCommand(SubjectCommand command);

//    void deleteById(Long recipeId, Long idToDelete);
}
