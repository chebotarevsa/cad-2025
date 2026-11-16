package ru.raspisa.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.raspisa.entity.MapAudio;
import ru.raspisa.entity.MapPlace;
import ru.raspisa.entity.MapImage;
import ru.raspisa.repository.MapAudioRepository;
import ru.raspisa.repository.MapPlaceRepository;
import ru.raspisa.repository.MapImageRepository;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@Transactional
public class MapPlaceService {
    private final MapPlaceRepository places;
    private final MapAudioRepository audio;
    private final MapImageRepository images;

    public MapPlaceService(MapPlaceRepository places, MapAudioRepository audio, MapImageRepository images) {
        this.places = places;
        this.audio = audio;
        this.images = images;
    }

    @Transactional(readOnly = true)
    public List<MapPlace> all() { return places.findAll(); }

    @Transactional(readOnly = true)
    public MapPlace find(long id) {
        return places.findById(id).orElseThrow(() -> new NoSuchElementException("Место не найдено"));
    }

    @Transactional(readOnly = true)
    public byte[] audio(long id) {
        MapPlace place = find(id);
        if (place.audioId == null) throw new NoSuchElementException("Песня не загружена");
        return audio.findById(place.audioId).orElseThrow(() -> new NoSuchElementException("Песня не найдена")).content;
    }

    @Transactional(readOnly = true)
    public byte[] image(long id) {
        MapPlace place = find(id);
        if (place.imageId == null) throw new NoSuchElementException("Изображение не загружено");
        return images.findById(place.imageId).orElseThrow(() -> new NoSuchElementException("Изображение не найдено")).content;
    }

    public MapPlace save(Long id, String title, String description, double latitude, double longitude,
                         MultipartFile file, boolean removeAudio, MultipartFile image, boolean removeImage, String icon) throws IOException {
        if (icon != null && !Set.of("pin", "school", "kindergarten", "hospital", "park", "church", "museum", "cafe", "music").contains(icon))
            throw new IllegalArgumentException("Выберите иконку из списка");
        if (title == null || title.isBlank() || title.strip().length() > 120)
            throw new IllegalArgumentException("Введите название до 120 символов");
        if (description == null || description.length() > 3000)
            throw new IllegalArgumentException("Описание должно быть не длиннее 3000 символов");
        if (!Double.isFinite(latitude) || !Double.isFinite(longitude)
                || latitude < -85 || latitude > 85 || longitude < -180 || longitude > 180)
            throw new IllegalArgumentException("Некорректные координаты");

        if (latitude < 50.745 || latitude > 50.840 || longitude < 36.420 || longitude > 36.535)
            throw new IllegalArgumentException("Выберите точку в Строителе или его ближайших окрестностях");
        byte[] picture = image == null || image.isEmpty() ? null : prepareImage(image);
        byte[] bytes = null;
        String type = null;
        if (file != null && !file.isEmpty()) {
            if (file.getSize() > 20 * 1024 * 1024) throw new IllegalArgumentException("Песня должна быть не больше 20 МБ");
            bytes = file.getBytes();
            type = audioType(bytes);
        }
        MapPlace place = id == null ? new MapPlace() : find(id);
        place.title = title.strip();
        place.description = description.strip();
        place.latitude = latitude;
        place.longitude = longitude;
        if (icon != null) place.icon = icon;
        if (removeImage || picture != null) {
            if (place.imageId != null) images.deleteById(place.imageId);
            place.imageId = null;
            place.imageName = null;
            place.imageType = null;
        }
        if (picture != null) {
            MapImage stored = new MapImage();
            stored.content = picture;
            place.imageId = images.save(stored).id;
            String name = image.getOriginalFilename();
            place.imageName = name == null ? "Изображение" : name.substring(0, Math.min(name.length(), 200));
            place.imageType = "image/png";
        }
        if (removeAudio || bytes != null) {
            if (place.audioId != null) audio.deleteById(place.audioId);
            place.audioId = null;
            place.audioName = null;
            place.audioType = null;
        }
        if (bytes != null) {
            MapAudio track = new MapAudio();
            track.content = bytes;
            place.audioId = audio.save(track).id;
            String name = file.getOriginalFilename();
            place.audioName = name == null ? "Песня" : name.substring(0, Math.min(name.length(), 200));
            place.audioType = type;
        }
        return places.save(place);
    }

    static String audioType(byte[] bytes) {
        String header = new String(bytes, 0, Math.min(bytes.length, 12), StandardCharsets.ISO_8859_1);
        if (header.startsWith("RIFF") && header.endsWith("WAVE")) return "audio/wav";
        if (header.startsWith("OggS")) return "audio/ogg";
        if (header.startsWith("ID3") || (bytes.length > 2 && (bytes[0] & 255) == 255 && (bytes[1] & 224) == 224)) return "audio/mpeg";
        throw new IllegalArgumentException("Выберите аудиофайл MP3, WAV или OGG");
    }

    private byte[] prepareImage(MultipartFile file) throws IOException {
        if (file.getSize() > 8 * 1024 * 1024) throw new IllegalArgumentException("Изображение должно быть не больше 8 МБ");

        try (var input = ImageIO.createImageInputStream(new ByteArrayInputStream(file.getBytes()))) {
            var readers = ImageIO.getImageReaders(input);
            if (!readers.hasNext()) throw new IllegalArgumentException("Выберите изображение JPG или PNG");
            var reader = readers.next();
            try {
                String format = reader.getFormatName();
                if (!format.equalsIgnoreCase("JPEG") && !format.equalsIgnoreCase("PNG"))
                    throw new IllegalArgumentException("Выберите изображение JPG или PNG");
                reader.setInput(input);
                int width = reader.getWidth(0), height = reader.getHeight(0);
                if (width < 1 || height < 1 || (long) width * height > 24_000_000)
                    throw new IllegalArgumentException("Изображение должно быть не больше 24 мегапикселей");
                BufferedImage original = reader.read(0);
                double scale = Math.min(1.0, 1280.0 / Math.max(width, height));
                BufferedImage resized = new BufferedImage(Math.max(1, (int) (width * scale)), Math.max(1, (int) (height * scale)), BufferedImage.TYPE_INT_ARGB);
                var graphics = resized.createGraphics();
                try {
                    graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    graphics.drawImage(original, 0, 0, resized.getWidth(), resized.getHeight(), null);
                } finally { graphics.dispose(); }
                var output = new ByteArrayOutputStream();
                ImageIO.write(resized, "png", output);
                return output.toByteArray();
            } finally { reader.dispose(); }
        } catch (IOException e) { throw new IllegalArgumentException("Не удалось прочитать изображение. Выберите другой JPG или PNG.", e); }
    }

    public void delete(long id) {
        MapPlace place = find(id);
        if (place.audioId != null) audio.deleteById(place.audioId);
        if (place.imageId != null) images.deleteById(place.imageId);
        places.delete(place);
    }
}
