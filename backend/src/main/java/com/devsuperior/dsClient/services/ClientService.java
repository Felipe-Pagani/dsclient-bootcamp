package com.devsuperior.dsClient.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dsClient.dto.ClientDTO;
import com.devsuperior.dsClient.entities.Client;
import com.devsuperior.dsClient.repositories.ClientRepository;
import com.devsuperior.dsClient.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository repository;

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
		Page<Client> list = repository.findAll(pageRequest);
		return list.map(x -> new ClientDTO(x));
		
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client client = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ClientDTO(client);
		
	}
	
	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client client = new Client();
		insertEntityClient(dto, client);
		return new ClientDTO(client);
	}
	

	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client client = repository.getOne(id);
			insertEntityClient(dto, client);
			return new ClientDTO(client);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		
	}
	
	private void insertEntityClient(ClientDTO dto, Client client) {
		client.setName(dto.getName());
		client.setCpf(dto.getCpf());
		client.setIncome(dto.getIncome());
		client.setBirthDate(dto.getBirthDate());
		client.setChildren(dto.getChildren());
		client = repository.save(client);
	}
	
	
	
	

}
