#include "AssetManager.hpp"

namespace Patrick {

	void  AssetManager::LoadTexture(std::string name, std::string fileName) {
		sf::Texture tex;

		if (tex.loadFromFile(fileName)) {
			this->_textures[name] = tex;
		}
	}
	
	sf::Texture& AssetManager::GetTexture(std::string name) {
		return this->_textures[name];
	}


	void  AssetManager::LoadFont(std::string name, std::string fileName) {
		sf::Font fonts;
		
		if (fonts.loadFromFile(fileName)) {
			this->_fonts[name] = fonts;
		}
	}
	sf::Font& AssetManager::GetFont(std::string name) {
		return this->_fonts[name];
	}
	

}