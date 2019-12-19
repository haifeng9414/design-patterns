package com.dhf.factory;

/**
 * Concrete subclass for creating new objects.
 */
public class ElfBlacksmith implements Blacksmith {

    // 这里参数为weaponType，这个参数应该是给具体的产品用的，和工厂创建哪个产品和如何创建产品没有关系
    // 即客户端需要一个带指定的weaponType属性的weapon，但是不知道具体的weapon类型，所以委托给某个客户端
    // 指定的Blacksmith铁匠来创建这个weapon，同时客户端将weaponType作为参数传给铁匠以达到将参数应用到
    // 最后返回的weapon中
    public Weapon manufactureWeapon(WeaponType weaponType) {
        return new ElfWeapon(weaponType);
    }

}
