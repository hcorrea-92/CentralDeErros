package com.v1.CentralDeErros.services;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.v1.CentralDeErros.Util.DateUtility;
import com.v1.CentralDeErros.exceptions.NotFoundException;
import com.v1.CentralDeErros.exceptions.WrongInputDataException;
import com.v1.CentralDeErros.models.ApplicationInstance;
import com.v1.CentralDeErros.models.Error;
import com.v1.CentralDeErros.models.DTOs.ApplicationInstanceDTO;
import com.v1.CentralDeErros.repositories.ApplicationInstanceRepository;

@Service
@Transactional
public class ApplicationInstanceService implements ApplicationInstanceServiceInterface {

    private final ApplicationInstanceRepository applicationInstanceRepository;

    @Autowired
    ApplicationInstanceService(ApplicationInstanceRepository applicationInstanceRepository) {
        this.applicationInstanceRepository = applicationInstanceRepository;
    }
    // Aqui acontece o mapeamento de uma applicationDTO para uma Entity ApplicationInstance.
    @Override
    public void addNew(ApplicationInstanceDTO applicationInstanceDTO) {
        Date instantiationDate;

        try {
            instantiationDate = DateUtility.getStringAsDate(applicationInstanceDTO.getInstantiationDate());
        } catch (ParseException ex) {
            throw new WrongInputDataException("A data de instanciação da aplicação não é válida");
        }

        ApplicationInstance newApplicationInstance = new ApplicationInstance(applicationInstanceDTO.getApplicationName(),
                instantiationDate);

        applicationInstanceRepository.save(newApplicationInstance);
    }

    @Override
    public List<ApplicationInstance> findAll(Integer size) {
        List<ApplicationInstance> returnList = applicationInstanceRepository.findAll();

        if (returnList.isEmpty()) {
            throw new NotFoundException("Não há instâncias de aplicações no banco de dados");
        }

        return returnList.stream()
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public List<Error> errors(Integer id) {
        Optional<ApplicationInstance> returnApplication = applicationInstanceRepository.findById(id);

        if (!returnApplication.isPresent()) {
            throw new NotFoundException("Não existe uma aplicação com o id informado");
        }

        if (returnApplication.get().getError().isEmpty()) {
            throw new NotFoundException("Não existem erros associados a essa aplicação");
        }

        return returnApplication.get().getError();
    }
}
