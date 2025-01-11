package br.com.campos.order.application.core.usecase.product;

import br.com.campos.order.adapters.in.controller.request.ProductRequest;
import br.com.campos.order.adapters.out.mapper.ProductMapper;
import br.com.campos.order.adapters.out.response.ProductResponse;
import br.com.campos.order.application.ports.in.product.ProductInputPort;
import br.com.campos.order.application.ports.out.product.CreateProductOutputPort;
import br.com.campos.order.application.ports.out.product.DeleteProductOutputPort;
import br.com.campos.order.application.ports.out.product.GetProductOutputPort;
import br.com.campos.order.application.ports.out.product.UpdateProductOutputPort;

import java.util.List;
import java.util.Optional;

public class ProductUseCase implements ProductInputPort {

    private CreateProductOutputPort createProductOutputPort;
    private UpdateProductOutputPort updateProductOutputPort;

    private GetProductOutputPort getProductOutputPort;

    private DeleteProductOutputPort deleteProductOutputPort;

    private ProductMapper productMapper;

    public ProductUseCase(CreateProductOutputPort createProductOutputPort,
                          UpdateProductOutputPort updateProductOutputPort,
                          GetProductOutputPort getProductOutputPort,
                          DeleteProductOutputPort deleteProductOutputPort,
                          ProductMapper productMapper) {

        this.createProductOutputPort = createProductOutputPort;
        this.updateProductOutputPort = updateProductOutputPort;
        this.getProductOutputPort = getProductOutputPort;
        this.deleteProductOutputPort = deleteProductOutputPort;
        this.productMapper = productMapper;
    }


    @Override
    public ProductResponse create(ProductRequest product){

        return createProductOutputPort.create(productMapper.toProduct(product));
    }

    @Override
    public Optional<ProductResponse> getProduct(Long id) {

        return getProductOutputPort.getProduct(id);
    }

    @Override
    public List<ProductResponse> getAllProducts() {

        return getProductOutputPort.getAllProducts();
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest product) {
        return updateProductOutputPort.updateProduct(id,productMapper.toProduct(product));
    }

    @Override
    public void deleteProduct(Long id) {

        deleteProductOutputPort.deleteProduct(id);
    }

}
