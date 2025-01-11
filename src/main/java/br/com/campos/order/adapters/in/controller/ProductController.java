package br.com.campos.order.adapters.in.controller;

import br.com.campos.order.adapters.in.controller.request.ProductRequest;
import br.com.campos.order.adapters.out.response.ProductResponse;
import br.com.campos.order.application.ports.in.product.ProductInputPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/product")
public class ProductController {

    private ProductInputPort productInputPort;

    public ProductController(ProductInputPort productInputPort) {
        this.productInputPort=productInputPort;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest product) {
        var productResponse = productInputPort.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ProductResponse>> getProduct(@PathVariable Long id) {
        Optional<ProductResponse> productResponse = productInputPort.getProduct(id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productInputPort.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productInputPort.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @Valid @RequestBody ProductRequest product) {
        ProductResponse updatedProduct = productInputPort.updateProduct(id,product);
        return ResponseEntity.ok(updatedProduct);
    }
}
