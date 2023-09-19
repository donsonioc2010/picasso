package picasso.server.api.admin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picasso.server.domain.domains.items.Picture;
import picasso.server.domain.domains.repository.PictureRepository;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {
    private final PictureRepository pictureRepository;


    public List<Picture> findAll() {
        return pictureRepository.findAll();
    }

    public Optional<Picture> findById(Long id) {
        return pictureRepository.findById(id);
    }
    public Optional<Picture> findByName(String pictureName) {
        return pictureRepository.findByName(pictureName);


}