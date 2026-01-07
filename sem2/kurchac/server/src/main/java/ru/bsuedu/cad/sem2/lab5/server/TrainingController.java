package ru.bsuedu.cad.sem2.lab5.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TrainingController {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    // Заглушка для отображения всех тренировок
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("trainings", trainingRepository.findAll()); // Получаем все тренировки
        return "index";
    }

    // Заглушка для добавления тренировки
    @PreAuthorize("hasRole('TRAINER')")
    @PostMapping("/addTraining")
    public String addTraining(@ModelAttribute Training training, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByName(userDetails.getUsername()).orElseThrow();
        
        // Заглушка для добавления тренировки, не подключаем функциональность тренера
        training.setTrainer(null);  // Устанавливаем тренера как null, так как не создаем полноценного тренера
        trainingRepository.save(training);
        return "redirect:/";  // После добавления перенаправляем на главную страницу
    }

    // Заглушка для удаления тренировки
    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/deleteTraining/{id}")
    public String deleteTraining(@PathVariable Long id) {
        trainingRepository.deleteById(id);  // Удаляем тренировку по id
        return "redirect:/";  // Перенаправляем на главную страницу
    }

    // Заглушка для обновления тренировки
    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/updateTraining/{id}")
    public String completeTraining(@PathVariable Long id, @RequestParam boolean completed) {
        Training training = trainingRepository.findById(id).orElseThrow();
        training.setCompleted(completed);  // Обновляем статус тренировки (завершена или нет)
        trainingRepository.save(training);
        return "redirect:/";  // Перенаправляем на главную страницу
    }

    // REST-методы (Заглушки)
    @GetMapping("/api")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('TRAINER')")
    public ResponseEntity<List<Training>> getAllTrainings() {
        List<Training> trainings = trainingRepository.findAll();
        return ResponseEntity.ok(trainings);  // Возвращаем все тренировки
    }

    @DeleteMapping("/api/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> deleteTrainingById(@PathVariable Long id) {
        trainingRepository.deleteById(id);  // Удаляем тренировку по id
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('TRAINER')")
    @PostMapping("/api")
    public ResponseEntity<Training> addTrainingFromApi(@RequestBody Training training,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Автор не найден"));

        // Заглушка для добавления тренировки через API
        training.setTrainer(null);  // Устанавливаем тренера как null
        Training savedTraining = trainingRepository.save(training);
        return ResponseEntity.status(201).body(savedTraining);
    }
}
