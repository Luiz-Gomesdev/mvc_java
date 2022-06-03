package br.com.luiz.gft.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.luiz.gft.model.Produto;
import br.com.luiz.gft.model.exception.ResourceNotFoundException;
import br.com.luiz.gft.repository.ProdutoRepository;
import br.com.luiz.gft.shared.ProdutoDTO;

@Service
public class ProdutoService {

	@Autowired
    private ProdutoRepository produtoRepository;

    /**
     * Metodo para Retornar uma lista de produtos
     * 
     * @return lista de produtos.
     */
    public List<ProdutoDTO> obterTodos() {

        // Retorna uma lista de produto model
        List<Produto> produtos = produtoRepository.findAll();

        return produtos.stream()
                .map(produto -> new ModelMapper().map(produto, ProdutoDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Metodo que retorna o produto encontrado pelo seu id
     * 
     * @param id do produto que sera localizado
     * @return Retorna um produto caso tenha encontrado.
     */
    public Optional<ProdutoDTO> obterPorId(Integer id) {
        // obtendo optional de produto por id
        Optional<Produto> produto = produtoRepository.findById(id);
        // se não encontrar lança exception
        if (produto.isEmpty()) {
            throw new ResourceNotFoundException("Produto com id: " + id + " não encontrado");
        }
        // convertendo o meu optional de produto em um produtoDTO
        ProdutoDTO dto = new ModelMapper().map(produto.get(), ProdutoDTO.class);

        // Criando e retornando um optional de produtoDTO
        return Optional.of(dto);
    }

    /**
     * Metodo para adicionar produto na lista.
     * 
     * @param produto que ser adicionado.
     * @return Retorna o produto que foi adicionado na lista.
     */
    public ProdutoDTO adicionar(ProdutoDTO produtoDto) {
        // Removendo o id para poder fazer o cadastro
        produtoDto.setId(null);

        // criando um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();
        // converter nosso produtoDTO em um produto
        Produto produto = mapper.map(produtoDto, Produto.class);
        // salvar o produto no banco
        produto = produtoRepository.save(produto);

        produtoDto.setId(produto.getId());
        // retornar o produtoDTO atualizado

        return produtoDto;
    }

    /**
     * Metodo para deletar produto por id.
     * 
     * @param id do produto a ser deletado.
     */
    public void deletar(Integer id) {
        // verificar se o produto existe
        Optional<Produto> produto = produtoRepository.findById(id);

        // Se não existir lança uma exception
        if (produto.isEmpty()) {
            throw new ResourceNotFoundException("Não foi possivel deletar o produto com id: " + id
                    + " Produto não existe");
        }
        // Deletar o produto pelo ID
        produtoRepository.deleteById(id);
    }

    /**
     * Metodo para atualizar o produto na lista
     * 
     * @param produto que sera atualizado
     * @param id      do produto
     * @return Retorna o produto após atualizar a lista
     */
    public ProdutoDTO atualizar(Integer id, ProdutoDTO produtoDto) {
        // Passar o id para o produtoDto.
        produtoDto.setId(id);
        // Criar um objeto de mapeamento.
        ModelMapper mapper = new ModelMapper();
        // Converter o Dto em um Produto.
        Produto produto = mapper.map(produtoDto, Produto.class);
        // Atualizar o produto no banco de dados.
        produtoRepository.save(produto);
        // Retornar o produtoDto atualizado.
        return produtoDto;
    }

}
