package com.transpo.controller;

import com.transpo.model.Bus;
import com.transpo.repository.BusRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/buses")
@CrossOrigin(origins = "*")
public class BusController {

    @Autowired
    private BusRepository busRepository;

    // ✅ Get all buses
    @GetMapping
    public ResponseEntity<List<Bus>> getAllBuses() {
        List<Bus> buses = busRepository.findAll();
        return ResponseEntity.ok(buses);
    }

    // ✅ Get single bus by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBusById(@PathVariable Long id) {
        Optional<Bus> bus = busRepository.findById(id);
        if (bus.isPresent()) {
            return ResponseEntity.ok(bus.get());
        } else {
            return ResponseEntity.badRequest().body("Bus not found");
        }
    }

    // ✅ Add a new bus and generate QR code
    @PostMapping("/add")
    public ResponseEntity<?> addBus(@RequestBody Bus bus) {
        try {
            Bus savedBus = busRepository.save(bus);

            // --- Generate QR Code ---
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            String qrData = "Bus ID: " + savedBus.getId()
                    + "\nBus Name: " + savedBus.getName()
                    + "\nRoute: " + savedBus.getRoute()
                    + "\nReg Number: " + savedBus.getRegNumber();

            BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, 250, 250);

            // Create folder if not exists
            Path dirPath = FileSystems.getDefault().getPath("qrcodes");
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            Path filePath = dirPath.resolve("bus_qr_" + savedBus.getId() + ".png");
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);

            // --- Optional: Save QR path in DB ---
            savedBus.setQrCodePath(filePath.toString());
            busRepository.save(savedBus);

            return ResponseEntity.ok(savedBus);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error generating QR code");
        }
    }

    // ✅ Generate QR and return as image
    @GetMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getBusQr(@PathVariable Long id) throws Exception {
        Optional<Bus> opt = busRepository.findById(id);
        if (opt.isEmpty()) throw new RuntimeException("Bus not found");
        Bus bus = opt.get();

        String data = "Bus ID: " + bus.getId()
                + "\nBus Name: " + bus.getName()
                + "\nRoute: " + bus.getRoute()
                + "\nReg Number: " + bus.getRegNumber();

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 300, 300);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out);
        return out.toByteArray();
    }

    // ✅ Delete bus by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBus(@PathVariable Long id) {
        if (busRepository.existsById(id)) {
            busRepository.deleteById(id);
            return ResponseEntity.ok("Bus deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Bus not found");
        }
    }
}
