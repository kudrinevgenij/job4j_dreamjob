package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryVacancyRepository implements VacancyRepository {
    private AtomicInteger nextId = new AtomicInteger(1);
    private final Map<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();

    private MemoryVacancyRepository() {

    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId.getAndIncrement());
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(), (id, oldVacancy) -> {
            return new Vacancy(oldVacancy.getId(), vacancy.getTitle(), vacancy.getDescription(), vacancy.getCreationDate(), vacancy.getVisible());
        }) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}