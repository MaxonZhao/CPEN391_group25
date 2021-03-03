#pragma once
#include <SFML/Graphics.hpp>
#include "Game.h"
#include <vector>
#include "DEFINITIONS.h"

namespace Patrick {
	class Land
	{
	public:
		Land(GameDataRef data);

		void MoveLand(float dt);
		void DrawLand();

		const std::vector<sf::Sprite>& GetSprites() const;

	private:
		GameDataRef _data;
		std::vector<sf::Sprite> _landSprites;
	};
}
