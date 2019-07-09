package ru.javawebinar.topjava.repository.datajpa;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;

import javax.persistence.NamedNativeQuery;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudUserRepository extends JpaRepository<User, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") int id);

    @Override
    @Transactional
    User save(User user);

    @Override
    Optional<User> findById(Integer id);

    @Override
    List<User> findAll(Sort sort);

    User getByEmail(String email);

    @EntityGraph(attributePaths = {"meals", "roles"})
    @Query("SELECT u FROM User u WHERE u.id=?1")
    User getWithMeals(int id);

    @Transactional
    @Modifying
    @Query(value = "DELETE from user_roles ur WHERE ur.user_id=:user_id AND ur.role=:role", nativeQuery = true)
    int deleteRole(@Param("user_id") String user_id, @Param("role") String role);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO user_roles (user_id, role) VALUES (:user_id, :role)", nativeQuery = true)
    int addRole(@Param("user_id") String user_id, @Param("role") String role);
}
