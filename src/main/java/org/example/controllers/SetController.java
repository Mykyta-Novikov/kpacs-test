package org.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.db.KPacsRepository;
import org.example.db.SetsRepository;
import org.example.db.records.KPacSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for {@code /sets/} and {@code /set/{id}/} pages, as well as adding and deleting K-PAC Sets.
 * Also has access to K-PACs and sets connection between them.
 */
@Controller
public class SetController {
    private final SetsRepository setsRepository;
    private final KPacsRepository kPacsRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public SetController(SetsRepository setsRepository, KPacsRepository kPacsRepository, ObjectMapper objectMapper) {
        this.setsRepository = setsRepository;
        this.kPacsRepository = kPacsRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Handler method for {@code /sets/} page.
     * @return Map of 2 serialized as JSON list of
     * {@link org.example.db.records.KPac} and {@link KPacSet} to be added to the {@link Model}.
     * @throws JsonProcessingException When return value cannot be properly serialized.
     */
    @GetMapping("/sets")
    public Map<String, String> sets() throws JsonProcessingException {
        return Map.of(
                "kpacs", objectMapper.writeValueAsString(kPacsRepository.getAll()),
                "sets", objectMapper.writeValueAsString(setsRepository.getAll())
        );
    }

    /**
     * Handler method for {@code /sets/{id}/} page.
     * @param id {@code id} of the K-Pac Set to be shown.
     * @return List of {@link org.example.db.records.KPac}, serialized as JSON.
     * @throws JsonProcessingException When return value cannot be properly serialized.
     */
    @GetMapping("/set/{id}")
    @ModelAttribute("setContents")
    public String setContents(@PathVariable int id) throws JsonProcessingException {
        return objectMapper.writeValueAsString(kPacsRepository.getBySet(id));

    }

    /**
     * Handler method for adding K-PAC Sets.
     * @param set Parsed request body. Uses {@code kPacIds}, does not use {@code id}.
     * @return Newly added {@link KPacSet} with {@code id} set by database.
     */
    @PostMapping(path = "/set")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public KPacSet addSet(@RequestBody KPacSet set) {
        return setsRepository.add(set);
    }

    /**
     * Handler Method for deleting K-PAC Sets.
     * @param id {@code id} of {@link KPacSet}.
     * @return Sets appropriate status code: {@code NO CONTENT} for success or {@code NOT FOUND} for failure.
     * Response body is not used.
     */
    @DeleteMapping("/set/{id}")
    public ResponseEntity<Void> deleteSet(@PathVariable int id) {
        var hasBeenDeleted = setsRepository.delete(id);
        return ResponseEntity.status(hasBeenDeleted ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).build();
    }
}
