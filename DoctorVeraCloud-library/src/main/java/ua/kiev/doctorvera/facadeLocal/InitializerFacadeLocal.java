package ua.kiev.doctorvera.facadeLocal;

import ua.kiev.doctorvera.entities.Identified;

import java.util.List;

/**
 * Created by volodymyr.bodnar on 28.11.2015.
 */
public interface InitializerFacadeLocal {
    Identified<Integer> initializeLazyEntity(Identified<Integer> entity);
    List<? extends Identified> initializeLazyEntity(List<? extends Identified> entityList);
}
