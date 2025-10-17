package com.MicroFinWay.service;

import com.MicroFinWay.model.Organization;
import com.MicroFinWay.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository repository;

    /**
     * Получает текущий операционный день.
     * В системе всегда одна организация, поэтому просто берём первую запись.
     */
    public LocalDate getCurrentOperationalDay() {
        return repository.findAll().get(0).getCurrentOperationalDay();
    }

    /**
     * Устанавливает дату текущего операционного дня.
     */
    public void setCurrentOperationalDay(LocalDate date) {
        Organization org = repository.findAll().get(0);
        org.setCurrentOperationalDay(date);
        repository.save(org);
    }

    /**
     * Устанавливает флаг закрытия операционного дня.
     */
    public void setOperationalDayClosed(boolean closed) {
        Organization org = repository.findAll().get(0);
        org.setOperationalDayClosed(closed);
        repository.save(org);
    }

    /**
     * Проверяет, закрыт ли текущий операционный день.
     */
    public boolean isOperationalDayClosed() {
        return repository.findAll().get(0).isOperationalDayClosed();
    }

    /**
     * Проверяет, закрыт ли конкретный день (для защиты от дублей).
     */
    public boolean isOperationalDayClosed(LocalDate date) {
        Organization org = repository.findAll().get(0);
        return org.isOperationalDayClosed()
                && org.getCurrentOperationalDay() != null
                && org.getCurrentOperationalDay().isEqual(date);
    }

    /**
     * Инициализация (один раз при первом запуске).
     * Если таблица пуста — создаём запись.
     */
    public void initializeIfNotExists() {
        if (repository.count() == 0) {
            Organization org = Organization.builder()
                    .name("Main Organization")
                    .currentOperationalDay(LocalDate.now())
                    .operationalDayClosed(false)
                    .build();
            repository.save(org);
        }
    }
}