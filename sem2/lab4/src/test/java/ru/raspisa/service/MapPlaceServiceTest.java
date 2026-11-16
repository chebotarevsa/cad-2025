package ru.raspisa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import ru.raspisa.repository.MapAudioRepository;
import ru.raspisa.repository.MapImageRepository;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:map-test;DB_CLOSE_DELAY=-1", "spring.jpa.show-sql=false"})
@Transactional
class MapPlaceServiceTest {
    @Autowired MapPlaceService service;
    @Autowired MapAudioRepository audio;
    @Autowired MapImageRepository images;

    @Test void chosenIconIsSavedAndCanBeChanged() throws Exception {
        var place = service.save(null, "Школа", "", 50.788, 36.483, null, false, null, false, "school");
        assertEquals("school", service.find(place.id).icon);
        service.save(place.id, "Школа", "Новое описание", 50.788, 36.483, null, false, null, false, null);
        assertEquals("school", service.find(place.id).icon, "Older clients must preserve the icon");
        service.save(place.id, "Больница", "", 50.788, 36.483, null, false, null, false, "hospital");
        assertEquals("hospital", service.find(place.id).icon);
        assertThrows(IllegalArgumentException.class, () -> service.save(place.id, "Место", "", 50.788, 36.483, null, false, null, false, "unknown"));
        assertEquals("hospital", service.find(place.id).icon);
    }

    @Test void createUpdateRemoveAudioAndDelete() throws Exception {
        byte[] bytes = "RIFF0000WAVEdata".getBytes(StandardCharsets.US_ASCII);
        var file = new MockMultipartFile("audio", "song.wav", "audio/wav", bytes);
        var place = service.save(null, "  Место  ", "Описание", 50.788, 36.483, file, false, null, false, null);
        long id = place.id;
        assertEquals("Место", service.find(id).title);
        assertArrayEquals(bytes, service.audio(id));
        assertEquals(1, audio.count());
        service.save(id, "Новое название", "", 50.79, 36.49, null, false, null, false, null);
        assertArrayEquals(bytes, service.audio(id));
        assertEquals(50.79, service.find(id).latitude);
        service.save(id, "Место", "", 50.79, 36.49, file, false, null, false, null);
        assertEquals(1, audio.count(), "Replacing audio should remove the previous blob");
        service.save(id, "Место", "", 50.79, 36.49, null, true, null, false, null);
        assertNull(service.find(id).audioId);
        assertEquals(0, audio.count());
        service.save(id, "Место", "", 50.79, 36.49, file, false, null, false, null);
        service.delete(id);
        assertTrue(service.all().isEmpty());
        assertEquals(0, audio.count());
    }

    @Test void rejectsInvalidCoordinatesAndDisguisedUploads() {
        assertThrows(IllegalArgumentException.class, () -> service.save(null, "Место", "", Double.NaN, 36, null, false, null, false, null));
        assertThrows(IllegalArgumentException.class, () -> service.save(null, "Место", "", 90, 36, null, false, null, false, null));
        assertThrows(IllegalArgumentException.class, () -> service.save(null, " ", "", 50, 36, null, false, null, false, null));
        var fake = new MockMultipartFile("audio", "song.mp3", "audio/mpeg", "<script>alert(1)</script>".getBytes());
        assertThrows(IllegalArgumentException.class, () -> service.save(null, "Место", "", 50.788, 36.483, fake, false, null, false, null));
        var large = new MockMultipartFile("audio", "large.mp3", "audio/mpeg", new byte[20 * 1024 * 1024 + 1]);
        assertThrows(IllegalArgumentException.class, () -> service.save(null, "Место", "", 50.788, 36.483, large, false, null, false, null));
        assertTrue(service.all().isEmpty());
    }

    private MockMultipartFile picture() throws Exception {
        var output = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(1600, 800, BufferedImage.TYPE_INT_RGB), "png", output);
        return new MockMultipartFile("image", "place.png", "image/png", output.toByteArray());
    }

    @Test void imageIsResizedPreservedReplacedAndRemoved() throws Exception {
        var place = service.save(null, "Фото", "", 50.788, 36.483, null, false, picture(), false, null);
        var decoded = ImageIO.read(new ByteArrayInputStream(service.image(place.id)));
        assertEquals(1280, decoded.getWidth());
        assertEquals(640, decoded.getHeight());
        assertEquals("image/png", place.imageType);
        Long original = place.imageId;
        service.save(place.id, "Фото", "Изменено", 50.788, 36.483, null, false, null, false, null);
        assertEquals(original, service.find(place.id).imageId);
        service.save(place.id, "Фото", "", 50.788, 36.483, null, false, picture(), false, null);
        assertNotEquals(original, service.find(place.id).imageId);
        assertEquals(1, images.count());
        service.save(place.id, "Фото", "", 50.788, 36.483, null, false, null, true, null);
        assertNull(service.find(place.id).imageId);
        assertEquals(0, images.count());
        service.save(place.id, "Фото", "", 50.788, 36.483, null, false, picture(), false, null);
        service.delete(place.id);
        assertEquals(0, images.count());
    }

    @Test void rejectsNonImagesOversizedImagesAndPlacesOutsideCity() throws Exception {
        var fake = new MockMultipartFile("image", "fake.png", "image/png", "<svg onload='alert(1)'/>".getBytes());
        assertThrows(IllegalArgumentException.class, () -> service.save(null, "Фото", "", 50.788, 36.483, null, false, fake, false, null));
        var large = new MockMultipartFile("image", "large.png", "image/png", new byte[8 * 1024 * 1024 + 1]);
        assertThrows(IllegalArgumentException.class, () -> service.save(null, "Фото", "", 50.788, 36.483, null, false, large, false, null));
        assertThrows(IllegalArgumentException.class, () -> service.save(null, "Далеко", "", 55.75, 37.61, null, false, null, false, null));
        assertEquals(0, images.count());
        assertTrue(service.all().isEmpty());
    }
}
