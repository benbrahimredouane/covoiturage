package ma.youcode.covoiturage.repository;

import ma.youcode.covoiturage.entity.Trajet;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TrajetSpecification {
    public static Specification<Trajet> hasStartVille(String startVille){
        return (root, query, criteriaBuilder) -> startVille == null
        ? criteriaBuilder.conjunction()
        :criteriaBuilder.equal(root.get("startVille"),startVille);
    }
    public static Specification<Trajet> hasEndVille(String endVille){
        return (root, query, criteriaBuilder) -> endVille == null
        ? criteriaBuilder.conjunction()
        : criteriaBuilder.equal(root.get("endVille"),endVille);
    }
    public static Specification<Trajet> hasDate(LocalDateTime dateTime){
        return (root, query, criteriaBuilder) -> dateTime == null
                ? criteriaBuilder.conjunction()
                :criteriaBuilder.equal(root.get("datetime"), dateTime);
    }
}
