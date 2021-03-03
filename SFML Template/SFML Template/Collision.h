#pragma once
#include <SFML/Graphics.hpp>

namespace Patrick {
	class Collision {
	public:
		Collision();
		bool CheckSpriteCollistion(sf::Sprite sprite1, sf::Sprite sprite2);
		bool CheckSpriteCollistion(sf::Sprite sprite1, float scale1,
			sf::Sprite sprite2, float scale2);
	};
}

