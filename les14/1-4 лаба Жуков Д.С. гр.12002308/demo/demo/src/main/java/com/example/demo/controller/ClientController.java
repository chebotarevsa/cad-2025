package com.example.demo.controller;

import com.example.demo.entity.Client;
import com.example.demo.controller.clientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private clientRepository clientRepository;

    // ========== ОТОБРАЖЕНИЕ КЛИЕНТОВ И ПОИСК ==========
    @GetMapping
    public String listClients(@RequestParam(name = "searchType", required = false) String searchType,
                              @RequestParam(name = "searchValue", required = false) String searchValue,
                              Model model) {

        List<Client> clients;
        String searchMessage = "";

        try {
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                // Ручная фильтрация (поиск)
                String trimmedValue = searchValue.trim().toLowerCase();
                List<Client> allClients = clientRepository.findAll();

                clients = allClients.stream()
                        .filter(client -> {
                            String type = (searchType != null) ? searchType : "lastName";
                            switch (type) {
                                case "lastName":
                                    return client.getLastName() != null &&
                                            client.getLastName().toLowerCase().contains(trimmedValue);
                                case "firstName":
                                    return client.getFirstName() != null &&
                                            client.getFirstName().toLowerCase().contains(trimmedValue);
                                case "phone":
                                    return client.getPhone() != null &&
                                            client.getPhone().contains(trimmedValue);
                                default:
                                    return true;
                            }
                        })
                        .collect(Collectors.toList());

                searchMessage = "Результаты поиска";
                logger.info("Search by {}: '{}', found {} clients", searchType, trimmedValue, clients.size());

            } else {
                // Все клиенты
                clients = clientRepository.findAll();
                searchMessage = "Все клиенты";
            }

        } catch (Exception e) {
            logger.error("Error loading clients: {}", e.getMessage(), e);
            clients = clientRepository.findAll();
            model.addAttribute("errorMessage", "Ошибка при загрузке клиентов");
            searchMessage = "Ошибка при выполнении поиска";
        }

        model.addAttribute("clients", clients);
        model.addAttribute("searchType", searchType != null ? searchType : "lastName");
        model.addAttribute("searchValue", searchValue != null ? searchValue : "");
        model.addAttribute("searchMessage", searchMessage);
        model.addAttribute("totalClients", clients.size());

        return "clients";
    }

    // ========== ДОБАВЛЕНИЕ КЛИЕНТА ==========
    @PostMapping("/add")
    public String addClient(@ModelAttribute Client client,
                            RedirectAttributes redirectAttributes) {
        try {
            logger.info("Adding client: {} {} ({})",
                    client.getLastName(), client.getFirstName(), client.getPhone());

            clientRepository.save(client);
            logger.info("Client added with ID: {}", client.getId());

            redirectAttributes.addFlashAttribute("successMessage",
                    "Клиент " + client.getLastName() + " " + client.getFirstName() + " успешно добавлен!");

        } catch (DataIntegrityViolationException e) {
            logger.error("Duplicate phone error: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ошибка: телефон " + client.getPhone() + " уже существует!");
        } catch (Exception e) {
            logger.error("Error adding client: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ошибка при добавлении клиента");
        }

        return "redirect:/clients";
    }

    // ========== УДАЛЕНИЕ КЛИЕНТА ==========
    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable("id") Long id,
                               RedirectAttributes redirectAttributes) {
        try {
            logger.info("Deleting client with id: {}", id);

            Client client = clientRepository.findById(id).orElse(null);

            if (client != null) {
                String clientName = client.getLastName() + " " + client.getFirstName();
                clientRepository.delete(client);
                logger.info("Client {} deleted", clientName);

                redirectAttributes.addFlashAttribute("successMessage",
                        "Клиент " + clientName + " успешно удалён!");
            } else {
                logger.warn("Client with id {} not found", id);
                redirectAttributes.addFlashAttribute("warningMessage",
                        "Клиент с ID " + id + " не найден!");
            }

        } catch (DataIntegrityViolationException e) {
            logger.error("Cannot delete client (foreign key constraint): {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Невозможно удалить клиента: он связан с автомобилями или заказами!");
        } catch (Exception e) {
            logger.error("Error deleting client: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ошибка при удалении клиента");
        }

        return "redirect:/clients";
    }

    // ========== ФОРМА РЕДАКТИРОВАНИЯ ==========
    @GetMapping("/edit/{id}")
    public String editClientForm(@PathVariable("id") Long id, Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            logger.info("Loading edit form for client id: {}", id);

            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Клиент не найден"));

            model.addAttribute("client", client);
            return "edit-client";

        } catch (Exception e) {
            logger.error("Error loading edit form: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ошибка при загрузке формы: " + e.getMessage());
            return "redirect:/clients";
        }
    }

    // ========== ОБНОВЛЕНИЕ КЛИЕНТА ==========
    @PostMapping("/update/{id}")
    public String updateClient(@PathVariable("id") Long id,
                               @ModelAttribute Client client,
                               RedirectAttributes redirectAttributes) {
        try {
            logger.info("Updating client with id: {}", id);

            // Проверяем существование клиента
            if (!clientRepository.existsById(id)) {
                throw new IllegalArgumentException("Клиент не найден");
            }

            // Устанавливаем ID (важно!)
            client.setId(id);

            // Сохраняем
            clientRepository.save(client);
            logger.info("Client updated successfully");

            redirectAttributes.addFlashAttribute("successMessage",
                    "Данные клиента успешно обновлены!");

        } catch (DataIntegrityViolationException e) {
            logger.error("Duplicate phone on update: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ошибка: телефон " + client.getPhone() + " уже используется другим клиентом!");
        } catch (Exception e) {
            logger.error("Error updating client: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Ошибка при обновлении клиента");
        }

        return "redirect:/clients";
    }
}