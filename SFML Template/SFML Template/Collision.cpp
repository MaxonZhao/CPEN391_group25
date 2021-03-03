#include "Collision.h"

namespace Patrick {
	Collision::Collision() {

	}
	bool Collision::CheckSpriteCollistion(sf::Sprite sprite1, sf::Sprite sprite2) {
		sf::Rect<float> reat1 = sprite1.getGlobalBounds();
		sf::Rect<float> reat2 = sprite2.getGlobalBounds();

		return reat1.intersects(reat2);
	}

	bool Collision::CheckSpriteCollistion(sf::Sprite sprite1, float scale1,
		sf::Sprite sprite2, float scale2) {
		sprite1.setScale(scale1, scale2);
		sprite2.setScale(scale1, scale2);
		sf::Rect<float> reat1 = sprite1.getGlobalBounds();
		sf::Rect<float> reat2 = sprite2.getGlobalBounds();

		return reat1.intersects(reat2);
	}
}
