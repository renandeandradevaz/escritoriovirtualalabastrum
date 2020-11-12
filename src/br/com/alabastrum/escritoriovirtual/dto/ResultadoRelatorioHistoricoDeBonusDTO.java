package br.com.alabastrum.escritoriovirtual.dto;

import br.com.alabastrum.escritoriovirtual.modelo.Usuario;

public class ResultadoRelatorioHistoricoDeBonusDTO {

    private ExtratoDTO extratoDTO;
    private Usuario usuarioRecebedorDoBonus;

    public ExtratoDTO getExtratoDTO() {
	return extratoDTO;
    }

    public void setExtratoDTO(ExtratoDTO extratoDTO) {
	this.extratoDTO = extratoDTO;
    }

    public Usuario getUsuarioRecebedorDoBonus() {
	return usuarioRecebedorDoBonus;
    }

    public void setUsuarioRecebedorDoBonus(Usuario usuarioRecebedorDoBonus) {
	this.usuarioRecebedorDoBonus = usuarioRecebedorDoBonus;
    }
}